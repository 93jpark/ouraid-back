package ouraid.ouraidback.dto.characters.responseDto;

import lombok.Builder;
import lombok.Data;
import ouraid.ouraidback.domain.Characters;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class SearchCharactersListResponse {
    List<SearchCharacterResponse> charList;

    public static List<SearchCharacterResponse> toDto(List<Characters> charList) {
        List<SearchCharacterResponse> list = new ArrayList<>();
        for(Characters ch : charList) {
            list.add(SearchCharacterResponse.toDto(ch));
        }
        return list;
    }
}
