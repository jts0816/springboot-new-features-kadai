package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;

	public FavoriteController (FavoriteRepository favoriteRepository, HouseRepository houseRepository) {
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
	}
	
	@GetMapping
	public String index (@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		
		Page<Favorite> favorite = favoriteRepository.findByUserOrderByCreatedAt(userDetailsImpl.getUser(), pageable);
		
		model.addAttribute("favoritePage", favorite);
		return "/favorite/index";
	}
	
	@GetMapping("/delete")
	public String delete (@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@RequestParam(name = "house", required = true) Integer houseId,
			RedirectAttributes redirectAttributes,
			Model model) {
		
		House house = houseRepository.getReferenceById(houseId);
		favoriteRepository.deleteByUserAndHouse(userDetailsImpl.getUser(), house);
		
		redirectAttributes.addFlashAttribute("message", "お気に入りを解除しました。");
		
		return "redirect:/favorite";
	}
	
}
