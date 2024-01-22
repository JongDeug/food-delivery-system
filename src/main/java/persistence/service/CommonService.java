package persistence.service;

import persistence.dao.MenuDAO;
import persistence.dao.OptionDAO;
import persistence.dao.StoreDAO;
import persistence.dto.MenuDTO;
import persistence.dto.OptionDTO;
import persistence.dto.StoreDTO;

import java.util.List;

public class CommonService {

    protected final StoreDAO storeDAO = StoreDAO.getInstance();
    protected final MenuDAO menuDAO = MenuDAO.getInstance();
    protected final OptionDAO optionDAO = OptionDAO.getInstance();

    /**
     * 공통 조회
     */
    public StoreDTO readStoreInfo(String storePk) {
        int pk = Integer.parseInt(storePk);
        StoreDTO storeDTO = null;
        // 조회 쿼리
        storeDTO = storeDAO.readStoreInfo(pk);
        return storeDTO;
    }

    public List<StoreDTO> readStoreInfoAll(){
        List<StoreDTO> storeDTOS = null;
        // 조회 쿼리
        storeDTOS = storeDAO.readStoreInfoAll();
        return storeDTOS;
    }

    public List<MenuDTO> readStoreMenuInfo(String storeFk) {
        int fk = Integer.parseInt(storeFk);
        List<MenuDTO> menuDTOS = null;
        // 조회 쿼리
        menuDTOS = menuDAO.readStoreMenuInfo(fk);
        return menuDTOS;
    }

    public List<OptionDTO> readStoreMenuOptionInfo(String storeFk) {
        int fk = Integer.parseInt(storeFk);
        List<OptionDTO> optionDTOS = null;
        // 조회 쿼리
        optionDTOS = optionDAO.readStoreMenuOptionInfo(fk);
        return optionDTOS;
    }
}
