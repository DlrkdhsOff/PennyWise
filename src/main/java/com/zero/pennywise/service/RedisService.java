package com.zero.pennywise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.pennywise.model.response.WaringMessageDTO;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService implements MessageListener {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      WaringMessageDTO waringMessage = mapper.readValue(message.getBody(), WaringMessageDTO.class);

      System.out.println("Received message body: " + new String(message.getBody()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
