package com.grcp.demo.votingapp.shared.external.message;

import com.grcp.demo.votingapp.shared.service.MessageSourceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class MessageSourceAdapterImpl implements MessageSourceAdapter {

    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object...args) {
        String[] argsAsString = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
        return messageSource.getMessage(
                code,
                argsAsString,
                code,
                Locale.getDefault());
    }
}
