package com.app.huntersclub

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.huntersclub.ui.slideshow.RegisterViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class RegisterUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAuth = mockk<FirebaseAuth>(relaxed = true)
    private val mockDb = mockk<FirebaseFirestore>(relaxed = true)

    @Test
    fun passwordTooShort_setsErrorMessage() {
        val viewModel = RegisterViewModel(mockAuth, mockDb)

        viewModel.register("User", "user@test.com", "123", "123")

        assertEquals(false, viewModel.registerResult.value)
        assertEquals("La contraseña debe tener al menos 6 caracteres.", viewModel.errorMessage.value)
    }

    @Test
    fun passwordMismatch_setsErrorMessage() {
        val viewModel = RegisterViewModel(mockAuth, mockDb)

        viewModel.register("User", "user@test.com", "123456", "654321")

        assertEquals(false, viewModel.registerResult.value)
        assertEquals("Las contraseñas no coinciden.", viewModel.errorMessage.value)
    }

    @Test
    fun validPasswordAndMatch_doesNotSetImmediateError() {
        val fakeTask = mockk<Task<AuthResult>>(relaxed = true)

        every { mockAuth.createUserWithEmailAndPassword(any(), any()) } returns fakeTask

        val viewModel = RegisterViewModel(mockAuth, mockDb)

        viewModel.register("User", "user@test.com", "123456", "123456")

        assertNull(viewModel.errorMessage.value)
    }
}
