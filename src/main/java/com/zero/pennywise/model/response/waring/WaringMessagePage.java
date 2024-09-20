package com.zero.pennywise.model.response.waring;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class WaringMessagePage {
  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<WaringMessageDTO> waringMessage;

  public static WaringMessagePage of(Page<WaringMessageDTO> pageList) {
    return new WaringMessagePage(
        pageList.getNumber() + 1,
        pageList.getTotalPages(),
        pageList.getTotalElements(),
        pageList.getContent()
    );

  }

}
