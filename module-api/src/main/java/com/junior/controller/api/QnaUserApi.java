package com.junior.controller.api;

import com.junior.dto.notice.NoticeUserDto;
import com.junior.dto.qna.QnaUserDto;
import com.junior.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Q&A User")
public interface QnaUserApi {


    @Operation(summary = "Q&A 조회", description = "Q&A를 Slice로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Q&A 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                          "customCode": "Q&A-SUCCESS-004",
                                                          "customMessage": "Q&A 조회 성공",
                                                          "status": true,
                                                          "data": {
                                                              "content": [
                                                                  {
                                                                      "id": 8,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:30.106743"
                                                                  },
                                                                  {
                                                                      "id": 7,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:29.341267"
                                                                  },
                                                                  {
                                                                      "id": 6,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:28.578246"
                                                                  },
                                                                  {
                                                                      "id": 5,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:27.830293"
                                                                  },
                                                                  {
                                                                      "id": 4,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:27.037108"
                                                                  },
                                                                  {
                                                                      "id": 3,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:26.369068"
                                                                  },
                                                                  {
                                                                      "id": 2,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:25.558126"
                                                                  },
                                                                  {
                                                                      "id": 1,
                                                                      "question": "질문",
                                                                      "answer": "답변",
                                                                      "createdDate": "2024-11-22T03:51:24.306963"
                                                                  }
                                                              ],
                                                              "pageable": {
                                                                  "pageNumber": 0,
                                                                  "pageSize": 8,
                                                                  "sort": {
                                                                      "empty": true,
                                                                      "sorted": false,
                                                                      "unsorted": true
                                                                  },
                                                                  "offset": 0,
                                                                  "paged": true,
                                                                  "unpaged": false
                                                              },
                                                              "size": 8,
                                                              "number": 0,
                                                              "sort": {
                                                                  "empty": true,
                                                                  "sorted": false,
                                                                  "unsorted": true
                                                              },
                                                              "numberOfElements": 8,
                                                              "first": true,
                                                              "last": true,
                                                              "empty": false
                                                          }
                                                      }
                                                    """
                                    )))
            })
    public CommonResponse<Slice<QnaUserDto>> findQna(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    );
}
