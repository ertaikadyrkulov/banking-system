package com.pro.auth_service.service;

import com.pro.auth_service.configuration.property.OtpProperties;
import com.pro.auth_service.dto.PhoneParts;
import com.pro.auth_service.dto.request.PhoneVerificationRequest;
import com.pro.auth_service.dto.request.VerifyOtpRequest;
import com.pro.auth_service.dto.response.RequestOtpResponse;
import com.pro.auth_service.dto.response.VerifyOtpResponse;
import com.pro.auth_service.exception.PhoneVerificationException;
import com.pro.auth_service.exception.model.Reason;
import com.pro.auth_service.util.AuthUtil;
import com.pro.auth_service.util.PhoneNumberData;
import com.pro.auth_service.util.PhoneUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PhoneVerificationService {

    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    private final OtpProperties properties;

    // keys
    private static final String FIELD_HASH = "hash";
    private static final String FIELD_ATTEMPTS = "attempts";

    // results
    private static final long RESULT_SUCCESS = 0;
    private static final long RESULT_EXPIRED = -1;
    private static final long RESULT_TOO_MANY_ATTEMPTS = -2;

    private static final DefaultRedisScript<Long> VERIFY_SCRIPT = new DefaultRedisScript<>("""
            local hash = redis.call('HGET', KEYS[1], 'hash')
            if not hash then return -1 end
            local used = tonumber(redis.call('HGET', KEYS[1], 'attempts'))
            local max = tonumber(ARGV[2])
            if used >= max then redis.call('DEL', KEYS[1]); return -2 end
            if hash == ARGV[1] then redis.call('DEL', KEYS[1]); return 0 end
            used = redis.call('HINCRBY', KEYS[1], 'attempts', 1)
            if used >= max then redis.call('DEL', KEYS[1]); return -2 end
            return used
            """, Long.class);

    public RequestOtpResponse requestOtp(PhoneVerificationRequest request) {
        PhoneNumberData phoneNumber = PhoneUtil.validatePhoneNumber(
                request.phoneCountryCode(),
                request.phoneNumber()
        );

        String phone = phoneNumber.nationalPhoneNumber();

        ensureNotLocked(phone);

        String cooldownKey = AuthUtil.cooldownKey(phone);

        Boolean acquired =  redisTemplate.opsForValue()
                .setIfAbsent(cooldownKey, "1", properties.resendCooldown());

        if(!Boolean.TRUE.equals(acquired)) {
            throw PhoneVerificationException.retryAfter(Reason.RESEND_COOLDOWN, remaining(cooldownKey));
        }

        String dailyKey = AuthUtil.dailyKey(phone);

        Long sends = redisTemplate.opsForValue()
                .increment(dailyKey);

        if (sends != null && sends == 1L) {
            redisTemplate.expire(dailyKey, Duration.ofDays(1));
        }

        if (sends != null && sends > properties.maxSendsPerDay()) {
            throw PhoneVerificationException.retryAfter(Reason.DAILY_LIMIT_EXCEEDED, remaining(dailyKey));
        }

        String code = AuthUtil.generateCode(properties.length());
        String codeKey = AuthUtil.codeKey(phone);

        redisTemplate.opsForHash()
                .putAll(
                        codeKey,
                        Map.of(FIELD_HASH, AuthUtil.hash(phone, code, properties.pepper()), FIELD_ATTEMPTS, "0")
                );
        redisTemplate.expire(codeKey, properties.ttl());

//        NotificationEvent event = NotificationEvent.of(
//                phoneNumber.phoneCountryCode(),
//                phoneNumber.phoneNumber(),
//                code
//        );
//
//        eventPublisher.publishEvent(event);

        return new RequestOtpResponse(properties.resendCooldown().toSeconds());
    }

    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        PhoneNumberData phoneNumber = PhoneUtil.validatePhoneNumber(
                request.phoneCountryCode(),
                request.phoneNumber()
        );

        String phone = phoneNumber.nationalPhoneNumber();
        String code = request.code();

        ensureNotLocked(phone);

        Long result = redisTemplate.execute(
                VERIFY_SCRIPT,
                List.of(AuthUtil.codeKey(phone)),
                AuthUtil.hash(phone, code, properties.pepper()),
                String.valueOf(properties.maxAttempts())
        );

        if (result == null || result == RESULT_EXPIRED) {
            throw PhoneVerificationException.of(Reason.CODE_EXPIRED);
        }
        if (result == RESULT_TOO_MANY_ATTEMPTS) {
            redisTemplate.opsForValue().set(
                    AuthUtil.lockKey(phone),
                    "1",
                    properties.lockDuration()
            );

            throw PhoneVerificationException.retryAfter(
                    Reason.TOO_MANY_ATTEMPTS,
                    properties.lockDuration()
            );
        }
        if (result > RESULT_SUCCESS) {
            throw PhoneVerificationException.invalidCode(
                    properties.maxAttempts() - result.intValue()
            );
        }

        String token = AuthUtil.generateToken();

        redisTemplate.opsForValue().set(
                AuthUtil.tokenKey(token),
                phoneNumber.phoneCountryCode() + "|" + phoneNumber.phoneNumber(),
                properties.verificationTokenTtl()
        );

        return new VerifyOtpResponse(
                token,
                properties.verificationTokenTtl().getSeconds()
        );
    }

    public PhoneParts consumeVerificationToken(String token) {
        String phone = redisTemplate.opsForValue().getAndDelete(AuthUtil.tokenKey(token));

        if (phone == null) {
            throw PhoneVerificationException.of(Reason.INVALID_VERIFICATION_TOKEN);
        }

        String[] parts = phone.split("\\|", 2);
        return new PhoneParts(parts[0], parts[1]);
    }

    private Duration remaining(String key) {
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if (ttl == null || ttl < 0) {
            return Duration.ZERO;
        }

        return Duration.ofSeconds(ttl);
    }

    private void ensureNotLocked(String phone) {
        Long ttl = redisTemplate.getExpire(AuthUtil.lockKey(phone), TimeUnit.SECONDS);
        if (ttl != null && ttl > 0) {
            throw PhoneVerificationException.retryAfter(Reason.LOCKED, Duration.ofSeconds(ttl));
        }
    }

}

