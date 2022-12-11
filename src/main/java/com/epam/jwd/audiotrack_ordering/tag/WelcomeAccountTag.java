package com.epam.jwd.audiotrack_ordering.tag;

import com.epam.jwd.audiotrack_ordering.entity.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

public class WelcomeAccountTag extends TagSupport {

    private static final long serialVersionUID = 6113723279256202939L;

    private static final Logger LOG = LogManager.getLogger(WelcomeAccountTag.class);

    private static final String PARAGRAPH_TAGS = "<p>%s, %s</p>";
    private static final String ACCOUNT_SESSION_PARAM_NAME = "account";

    private String text;

    @Override
    public int doStartTag() {
        extractAccountNameFromSession()
                .ifPresent(name -> printTextOut(format(PARAGRAPH_TAGS, text, name)));
        return SKIP_BODY;
    }

    private Optional<String> extractAccountNameFromSession() {
        return Optional.ofNullable(pageContext.getSession())
                .map(session -> (Account) session.getAttribute(ACCOUNT_SESSION_PARAM_NAME))
                .map(Account::getLogin);
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    private void printTextOut(String text) {
        final JspWriter out = pageContext.getOut();
        try {
            out.write(text);
        } catch (IOException e) {
            LOG.error("error occurred writing text to jsp.text: {}, tagId: {}", text, id);
            LOG.error(e);
        }
    }

    public void setText(String text) {
        this.text = text;
    }
}
