package com.tfdor.core.interceptor;

import com.tfdor.core.configurer.PropertyConfigurer;
import com.tfdor.tools.dicts.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tfdor.domain.manage.UserInfo;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 复合交易list
 *
 * @author chepeiqing
 * @version V1.0.0
 * @Mail chepeiqin@icloud.com
 * @Date 16/10/18
 * @Time 上午10:05
 */
public class AuditingInterceptor {
    private static Logger logger = LoggerFactory.getLogger(AuditingInterceptor.class);
    private List<String> auditignList;
    @Resource
    private PropertyConfigurer propertyConfigurer;

    public List<String> getAuditignList() {
        return auditignList;
    }

    public boolean auditing(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getRequestURI();
        String transName = request.getParameter("transName");
        for (String actn : getAuditignList()) {
            if (action.indexOf(actn) > 0 && !"audiAgree.do".equals(transName)) {
                logger.debug(actn + "交易需复合,提交复合模版");
                String acName = propertyConfigurer.getMessage(actn);
                UserInfo userinfo = (UserInfo) request.getSession().getAttribute(Dict.SESSIONUSERID);
                request.getSession().setAttribute("auditig"+userinfo.getUserSeq(),actn+","+acName);
                request.getRequestDispatcher("/addAuditing.do").forward(request, response);
                return false;
            }
        }
        return true;
    }

    public void setAuditignList(List<String> auditignList) {
        this.auditignList = auditignList;
    }
}
