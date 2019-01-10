@file:Suppress("IllegalIdentifier")

package com.sanogueralorenzo.posts.datasource.cache

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanogueralorenzo.cache.Cache
import com.sanogueralorenzo.posts.comment
import com.sanogueralorenzo.posts.domain.model.Comment
import com.sanogueralorenzo.posts.post
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class CommentCacheDataSourceImplTest {

    private lateinit var dataSource: CommentCacheDataSourceImpl

    private val mockCache: Cache<List<Comment>> = mock()

    val key = "Comment List"

    private val postId = post.id

    private val cacheItem = comment.copy(name = "cache")
    private val remoteItem = comment.copy(name = "remote")

    private val cacheList = listOf(cacheItem)
    private val remoteList = listOf(remoteItem)

    private val throwable = Throwable()

    @Before
    fun setUp() {
        dataSource = CommentCacheDataSourceImpl(mockCache)
    }

    @Test
    fun `get comments cache success`() {
        // given
        whenever(mockCache.load(key + postId)).thenReturn(Single.just(cacheList))

        // when
        val test = dataSource.get(postId).test()

        // then
        verify(mockCache).load(key + postId)
        test.assertValue(cacheList)
    }

    @Test
    fun `get comments cache fail`() {
        // given
        whenever(mockCache.load(key + postId)).thenReturn(Single.error(throwable))

        // when
        val test = dataSource.get(postId).test()

        // then
        verify(mockCache).load(key + postId)
        test.assertError(throwable)
    }

    @Test
    fun `set comments cache success`() {
        // given
        whenever(mockCache.save(key + postId, remoteList)).thenReturn(Single.just(remoteList))

        // when
        val test = dataSource.set(postId, remoteList).test()

        // then
        verify(mockCache).save(key + postId, remoteList)
        test.assertValue(remoteList)
    }

    @Test
    fun `set comments cache fail`() {
        // given
        whenever(mockCache.save(key + postId, remoteList)).thenReturn(Single.error(throwable))

        // when
        val test = dataSource.set(postId, remoteList).test()

        // then
        verify(mockCache).save(key + postId, remoteList)
        test.assertError(throwable)
    }
}
