package com.khao.PoorDeal.domain;

/**
 * @file CommentStatus.java
 * @brief 댓글의 상태를 나타내는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
public enum CommentStatus {

	/** @brief 활성 상태의 댓글 */
	ACTIVE(),
	/** @brief 수정된 댓글 */
	MODIFIED(),
	/** @brief 삭제된 댓글 */
	DELETED();
}
