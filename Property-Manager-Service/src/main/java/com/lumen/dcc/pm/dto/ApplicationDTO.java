package com.lumen.dcc.pm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDTO {


		private long applicationId;
		
		@NotBlank(message="Application name is required")
		private String name;
		
		@NotBlank(message="Application description is required")
		private String description;

		public ApplicationDTO() {
		}

		public ApplicationDTO(long applicationId, @NotBlank(message = "Application name is required") String name,
				@NotBlank(message = "Application description is required") String description) {
			super();
			this.applicationId = applicationId;
			this.name = name;
			this.description = description;
		}

		public long getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(Long long1) {
			this.applicationId = long1;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
			

}

