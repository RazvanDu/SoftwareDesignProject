package com.Project.OrderService.Utilities;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Utils {

    public static String getDataURL(String data) {

        return "http://127.0.0.1:1113/" + data;

    }

    public static Issue getIssueFromJson(JsonObject jsonObject) {

        Issue issue = new Issue();

        issue.setId(jsonObject.get("id").getAsInt());
        issue.setTitle(jsonObject.get("title").getAsString());
        issue.setPrice(jsonObject.get("price").getAsDouble());;
        issue.setReview(jsonObject.get("review").getAsInt());
        issue.setIssue(jsonObject.get("issue").getAsInt());

        issue.setCover(jsonObject.get("cover") == null ? "" : jsonObject.get("cover").getAsString());
        issue.setLinkPreview(jsonObject.get("linkPreview") == null ? "" : jsonObject.get("linkPreview").getAsString());

        issue.setWriterId(jsonObject.get("writer") == null ? null : jsonObject.get("writer").getAsJsonObject());
        issue.setSeriesId(jsonObject.get("series") == null ? null : jsonObject.get("series").getAsJsonObject());

        return issue;

    }

    public static String getIssues(HashMap<Integer, Issue> cart) {

        return cart.values().stream()
                .mapToInt(Issue::getId).mapToObj(String::valueOf).collect(Collectors.joining(","));

    }

    public static String getAmounts(HashMap<Integer, Issue> cart) {

        return cart.values().stream()
                .mapToInt(Issue::getAmount).mapToObj(String::valueOf).collect(Collectors.joining(","));

    }
}
