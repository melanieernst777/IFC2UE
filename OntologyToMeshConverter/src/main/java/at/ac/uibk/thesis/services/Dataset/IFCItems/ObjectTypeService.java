package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.enums.ObjectType;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Scope("application")
public class ObjectTypeService {

    /**
     * for unreal engine, we can define which items the user should be able to open
     * for now, those items are doors and windows
     *
     * @param item the item of which we want to have the information whether the item can be opened
     * @return true if the item can be opened
     */
    public boolean canObjectBeOpened(Item item) {
        return item.getObjectType().equals(ObjectType.DOOR) || item.getObjectType().equals(ObjectType.WINDOW);
    }
}
