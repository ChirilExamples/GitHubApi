package com.example.githubapi.data.data_structure

data class GitHubStructureItem(

    val description: String,
    val html_url: String,
    val id: Int,
    val name: String,
    val owner: Owner
)