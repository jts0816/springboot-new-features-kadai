package com.example.samuraitravel.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRegisterForm {
	
	@Min(value = 1, message = "評価を入力してください。")
	private int evaluation;

	@NotBlank(message = "コメントを入力してください。")
	private String reviewText;

}
