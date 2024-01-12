package com.example.samuraitravel.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
	
	private int id;
	
	@Min(value = 1, message = "評価を入力してください。")
	private int evaluation;

	@NotBlank(message = "コメントを入力してください。")
	private String reviewText;
}
