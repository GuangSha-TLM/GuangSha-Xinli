package com.example.demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.Instant;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import javax.servlet.http.HttpSession;
import com.example.demo.domain.dto.User;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间、创建人
        this.strictInsertFill(metaObject, "createAt", Long.class, Instant.now().toEpochMilli());
        this.strictInsertFill(metaObject, "createBy", String.class, getCurrentUserId());
        this.strictInsertFill(metaObject, "status", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充更新时间、修改人
        this.strictUpdateFill(metaObject, "updateAt", Long.class, Instant.now().toEpochMilli());
        this.strictUpdateFill(metaObject, "updateBy", String.class, getCurrentUserId());
    }

    private String getCurrentUserId() {
        try {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpSession session = (HttpSession) attrs.resolveReference(RequestAttributes.REFERENCE_SESSION);
                if (session != null) {
                    Object userObj = session.getAttribute("user");
                    if (userObj instanceof User) {
                        User user = (User) userObj;
                        if (user.getId() != null) {
                            return String.valueOf(user.getId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return "anonymous";
    }
}
