package pros.ElectronicStore.helper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public  static <U,V> PageableResponse<V> getPageResponse(Page<U> pages, Class<V> type){
        List<U> entity = pages.getContent();
        List<V> userDto = entity.stream().map((object)->new ModelMapper().map(object,type)
        ).toList();
        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(userDto);
        response.setPageNumber(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setTotalElements(pages.getTotalElements());
        response.setLastPage(pages.isLast());

        return response;
    }

}
