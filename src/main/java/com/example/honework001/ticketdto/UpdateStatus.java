package com.example.honework001.ticketdto;
import lombok.Builder;
import java.util.List;
@Builder
public record UpdateStatus(
        List<Integer> ticketIds,
        Boolean paymentStatus
)
{

}
