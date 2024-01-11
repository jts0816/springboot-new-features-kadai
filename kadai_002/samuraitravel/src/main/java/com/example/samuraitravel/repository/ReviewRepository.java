package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	public Page<Review> findByHouseOrderByUpdatedAtDesc(House house, Pageable pageable);
	
	public List<Review> findTop6ByHouseOrderByUpdatedAtDesc(House house);
	
	public Review getByUserAndHouse(User use, House house);

}
