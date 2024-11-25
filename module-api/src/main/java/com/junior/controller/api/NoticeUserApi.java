package com.junior.controller.api;

import com.junior.dto.notice.NoticeUserDto;
import com.junior.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface NoticeUserApi {


    @Operation(summary = "공지사항 조회", description = "공지사항을 Slice로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지사항 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                          "customCode": "NOTICE-SUCCESS-004",
                                                          "customMessage": "공지사항 조회 성공",
                                                          "status": true,
                                                          "data": {
                                                              "content": [
                                                                  {
                                                                      "id": 8,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
                                                                      "createdDate": "2024-11-22T03:51:30.106743"
                                                                  },
                                                                  {
                                                                      "id": 7,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
                                                                      "createdDate": "2024-11-22T03:51:29.341267"
                                                                  },
                                                                  {
                                                                      "id": 6,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
                                                                      "createdDate": "2024-11-22T03:51:28.578246"
                                                                  },
                                                                  {
                                                                      "id": 5,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
                                                                      "createdDate": "2024-11-22T03:51:27.830293"
                                                                  },
                                                                  {
                                                                      "id": 4,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
                                                                      "createdDate": "2024-11-22T03:51:27.037108"
                                                                  },
                                                                  {
                                                                      "id": 3,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
                                                                      "createdDate": "2024-11-22T03:51:26.369068"
                                                                  },
                                                                  {
                                                                      "id": 2,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
                                                                      "createdDate": "2024-11-22T03:51:25.558126"
                                                                  },
                                                                  {
                                                                      "id": 1,
                                                                      "title": "타이틀",
                                                                      "content": "내용",
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
    public CommonResponse<Slice<NoticeUserDto>> findNotice(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam("size") int size
    );
}
