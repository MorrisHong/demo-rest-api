package kr.gracelove.demorestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.BindingResult;

import java.io.IOException;

@JsonComponent //objectMapper에 등록
public class ErrorsSerializer extends JsonSerializer<BindingResult> {

	@Override
	public void serialize(BindingResult bindingResult, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartArray();
		/**
		 * stream()에서 forEach만 할꺼면 stream() 생략 가능.
		 */
		bindingResult.getFieldErrors().forEach(e -> {
			try {
				jsonGenerator.writeStartObject();
				jsonGenerator.writeStringField("field", e.getField());
				jsonGenerator.writeStringField("objectName", e.getObjectName());
				jsonGenerator.writeStringField("code", e.getCode());
				jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
				Object rejectedValue = e.getRejectedValue();
				if(rejectedValue != null) {
					jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
				}
				jsonGenerator.writeEndObject();
			} catch (IOException el) {
				el.printStackTrace();
			}
		});
		bindingResult.getGlobalErrors().forEach( e -> {
			try {
				jsonGenerator.writeStartObject();
				jsonGenerator.writeStringField("objectName", e.getObjectName());
				jsonGenerator.writeStringField("code", e.getCode());
				jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
				jsonGenerator.writeEndObject();
			} catch (IOException el) {
				el.printStackTrace();
			}
		});
		jsonGenerator.writeEndArray();
	}
}