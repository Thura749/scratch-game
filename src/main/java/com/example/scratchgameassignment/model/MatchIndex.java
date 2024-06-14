package com.example.scratchgameassignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchIndex {
    private int row;
    private int column;

    public boolean compareIndex(MatchIndex other) {
        return (this.getRow()==other.getRow() && this.getColumn()== other.getColumn());
    }
}
