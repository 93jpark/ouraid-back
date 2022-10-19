package ouraid.ouraidback.dto.characters.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateCharacterListRequest {
    private List<CreateCharacterRequest> charList;

    public CreateCharacterListRequest() {
        charList = new ArrayList<>();
    }
}
