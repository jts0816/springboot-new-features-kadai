package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
@RequestMapping("/houses/{id}/review")
public class ReviewController {

	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;

	public ReviewController(HouseRepository houseRepository, ReviewRepository reviewRepository) {
		this.houseRepository = houseRepository;
		this.reviewRepository = reviewRepository;
	}

	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PathVariable(name = "id") Integer id,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		House house = houseRepository.getReferenceById(id);
		Page<Review> reviewPage = reviewRepository.findByHouseOrderByUpdatedAtDesc(house, pageable);
        Integer userId = null;
        boolean isPosted = false;
        if (userDetailsImpl != null) {
        	userId = userDetailsImpl.getUser().getId();
            Review userReview = reviewRepository.getByUserAndHouse(userDetailsImpl.getUser(), house);
        	isPosted = userReview != null;
        }
        
		model.addAttribute("house", house);
        model.addAttribute("userId", userId);
        model.addAttribute("isPosted", isPosted);
		model.addAttribute("reviewPage", reviewPage);
		return "houses/review/index";		
	}

	@GetMapping("/register")
	public String register(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		model.addAttribute("house", house);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		return "houses/review/register";
	}

	@GetMapping("/edit")
	public String edit(@RequestParam(name = "id", required = false) Integer reviewId,  Model model) {
		Review review = reviewRepository.getReferenceById(reviewId);
		ReviewEditForm reviewEditForm = new ReviewEditForm(reviewId, review.getEvaluation(), review.getReviewText());
		model.addAttribute("house", review.getHouse());
		model.addAttribute("reviewEditForm", reviewEditForm);
		return "houses/review/edit";
	}

	
	@PostMapping("/register/save")
	public String add(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PathVariable(name = "id") Integer houseId,
			@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();

		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			return "/houses/review/register";
		}

		Review review = new Review();
		review.setUser(user);
		review.setHouse(house);
		review.setEvaluation(reviewRegisterForm.getEvaluation());
		review.setReviewText(reviewRegisterForm.getReviewText());
		reviewRepository.save(review);

		redirectAttributes.addFlashAttribute("message", "レビューを投稿しました。");
		return "redirect:/houses/{id}/review";
	}

	@PostMapping("/edit/save")
	public String update(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PathVariable(name = "id") Integer houseId,
			@ModelAttribute @Validated ReviewEditForm reviewEditForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();

		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			return "/houses/review/edit";
		}

		Review review = new Review();
		review.setId(reviewEditForm.getId());
		review.setUser(user);
		review.setHouse(house);
		review.setEvaluation(reviewEditForm.getEvaluation());
		review.setReviewText(reviewEditForm.getReviewText());
		reviewRepository.save(review);

		redirectAttributes.addFlashAttribute("message", "レビューを編集しました。");
		return "redirect:/houses/{id}/review";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam(name = "reviewId", required = false) Integer reviewId,
			@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		
		reviewRepository.deleteById(reviewId);

		redirectAttributes.addFlashAttribute("message", "レビューを削除しました。");
		return "redirect:/houses/{id}/review";
	}

}
