package pros.ElectronicStore.dtos;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse <T>{

    private  List<T> content;
    private int pageNumber;
    private int pageSize;
    private long TotalPages;
    private long TotalElements;
    private boolean LastPage;
}
