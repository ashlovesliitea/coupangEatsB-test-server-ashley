package com.example.demo.src.interceptor;

import com.example.demo.annotation.NoAuth;
import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        boolean check=checkAnnotation(handler, NoAuth.class);
        if(check) return true;
        try{
            int userNumByJwt=jwtService.getUserIdx();
            request.setAttribute("userIdx",userNumByJwt);

        }catch(BaseException e){
            String requestURI=request.getRequestURI();
            Map<String,String> map=new HashMap<>();
            map.put("requestURI","/app/users/sign-in?redirectURL="+requestURI);
            String json=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            response.getWriter().write(json);
            return false;
        }

        return true;
    }

    private boolean checkAnnotation(Object handler,Class cls){
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        if(handlerMethod.getMethodAnnotation(cls)!=null){ //해당 어노테이션이 존재하면 true.
            return true;
        }
        return false;
    }
}
