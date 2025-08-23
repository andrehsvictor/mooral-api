package io.github.andrehsvictor.mooral.api.shared.message.email;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendActionEmailMessage {

    private String name;
    private String email;
    private String url;
    private String token;
    private Map<String, Object> extra;

}
