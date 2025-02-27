package movieweb.movieweb.dtos.persons;

import movieweb.movieweb.enums.VoteType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieweb.movieweb.dtos.roles.PersonMovieRoleDto;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.enums.Role;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
public class UserVoteStatusDto {
    private boolean hasVoted;
    private VoteType voteType;

    public UserVoteStatusDto(boolean hasVoted, VoteType voteType) {
        this.hasVoted = hasVoted;
        this.voteType = voteType;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public VoteType getVoteType() {
        return voteType;
    }
}