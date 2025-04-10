package io.github.xyonly.ward.dao;

import lombok.Getter;

/**
 * ResponseDto is a values container for presenting response info
 *
 * @author Rudolf Barbu
 * @version 1.0.0
 */
@Getter
public final class ResponseDto
{
    /**
     * Response message field
     */
    private final String message;

    /**
     * Setter for message field
     *
     * @param message message to display
     */
    public ResponseDto(final String message)
    {
        this.message = message;
    }
}