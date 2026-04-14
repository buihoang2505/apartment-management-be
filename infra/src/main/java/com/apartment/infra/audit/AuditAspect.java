package com.apartment.infra.audit;

import com.apartment.domain.audit.AuditLog;
import com.apartment.domain.audit.AuditLogRepository;
import com.apartment.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Around("execution(* com.apartment.app.*.handler.*CommandHandler.handle(..))" +
            " || execution(* com.apartment.app.*.handler.*CommandHandler.delete*(..))")
    public Object auditCommand(ProceedingJoinPoint pjp) throws Throwable {
        String action = resolveAction(pjp);
        Object[] args = pjp.getArgs();
        String newValue = args.length > 0 && args[0] != null ? args[0].toString() : null;

        Object result = pjp.proceed();

        try {
            AuditLog log = AuditLog.builder()
                    .user(getCurrentUser())
                    .action(action)
                    .newValue(newValue)
                    .build();
            auditLogRepository.save(log);
        } catch (Exception ex) {
            // audit failure must not affect the main operation
            log.warn("Failed to write audit log for action {}: {}", action, ex.getMessage());
        }

        return result;
    }

    private String resolveAction(ProceedingJoinPoint pjp) {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String handlerClass = pjp.getTarget().getClass().getSimpleName();
        String methodName = sig.getName();
        return handlerClass + "." + methodName;
    }

    private com.apartment.domain.user.User getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) return null;
            return userRepository.findByUsername(auth.getName()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}