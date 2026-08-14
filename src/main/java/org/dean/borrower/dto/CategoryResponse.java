package org.dean.borrower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//Constructors for the category response object
//setting up what is needed for each
@Getter
@AllArgsConstructor
public class CategoryResponse {
    private final Long id;
    private final String name;
    private final String description;

    //constructor

}

