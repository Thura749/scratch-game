package com.example.scratchgameassignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class WinCombinationDto {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;
    @JsonProperty("when")
    private String combinationWhen;
    @JsonProperty("count")
    private int combinationCount;
    @JsonProperty("group")
    private String combinationGroup;
    private List<List<MatchIndex>> coveredAreas;

    @JsonProperty("covered_areas")
    public void setCoveredAreas(List<List<String>> coveredAreas) {
        List<List<MatchIndex>> covered = new ArrayList<>();
        coveredAreas.forEach(area-> {
            var rowCol = area.stream().filter(str-> Objects.nonNull(str) && str.contains(":"))
                    .map(str-> str.split(":"))
                    .filter(strArr-> strArr.length > 1)
                    .map(strArr-> new MatchIndex(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1])))
                    .toList();
            covered.add(rowCol);
        });
        this.coveredAreas = covered;
    }
}
