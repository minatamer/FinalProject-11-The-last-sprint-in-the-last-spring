package com.example.SearchApp.Strategy;

import com.example.SearchApp.model.PostDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("postSearchStrategy")
public class PostSearchStrategy implements SearchStrategy<PostDTO>
{

    
    @Override
    public List<PostDTO> search(List<PostDTO> posts, String query) {
        List<PostDTO> relevantPosts = new ArrayList<>();
        for (PostDTO post : posts) {
            if (post.getTextContent().toLowerCase().contains(query.toLowerCase())) {
                relevantPosts.add(post);
            }
        }

        return relevantPosts;
    }

}
