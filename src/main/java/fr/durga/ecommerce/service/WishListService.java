package fr.durga.ecommerce.service;

import java.util.List;

import fr.durga.ecommerce.model.WishList;
import fr.durga.ecommerce.repository.WishListRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
@Transactional
public class WishListService {

    private final WishListRepository wishListRepository;

    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public void createWishlist(WishList wishList) {
        wishListRepository.save(wishList);
    }

    public List<WishList> readWishList(Integer userId) {
        return wishListRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }
}

