package com.grcp.demo.votingapp.vote.entrypoint.mapper;

import com.grcp.demo.votingapp.vote.domain.AggregatedVotingResult;
import com.grcp.demo.votingapp.vote.domain.VotingResult;
import com.grcp.demo.votingapp.vote.entrypoint.model.AggregatedVotingResultResponseDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VotingResultResponseDto;

import java.util.List;

public class VoteDtoMapper {

    public static AggregatedVotingResultResponseDto toAggregatedVotingResultResponseDto(AggregatedVotingResult aggregatedVotingResult) {
        List<VotingResultResponseDto> votingResultsResponse = toVotingResultsResponseDto(aggregatedVotingResult);
        return VoteDtoMapper.toAggregatedVotingResultResponseDto(
                aggregatedVotingResult,
                votingResultsResponse);
    }

    private static AggregatedVotingResultResponseDto toAggregatedVotingResultResponseDto(
            AggregatedVotingResult aggregatedVotingResult,
            List<VotingResultResponseDto> votingResultsResponse) {
        return new AggregatedVotingResultResponseDto(
                aggregatedVotingResult.totalPoolVotes(),
                votingResultsResponse);
    }

    private static List<VotingResultResponseDto> toVotingResultsResponseDto(
            AggregatedVotingResult aggregatedVotingResult) {
        return aggregatedVotingResult.votingResults().stream()
                .map(VoteDtoMapper::toVotingResultResponseDto)
                .toList();
    }

    private static VotingResultResponseDto toVotingResultResponseDto(VotingResult votingResult) {
        return new VotingResultResponseDto(
                votingResult.poolOptionId().value(),
                votingResult.description(),
                votingResult.totalVotes(),
                votingResult.percentage());
    }
}
