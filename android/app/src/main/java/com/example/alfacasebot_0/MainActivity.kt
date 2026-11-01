package com.example.alfacasebot_0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alfacasebot_0.ui.theme.AlfaCaseBot_0Theme
import ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlfaCaseBot_0Theme {
                val vm: AppViewModel = viewModel()
                BackHandler(
                    enabled = vm.isLogged && vm.currentScreen != Screen.HOME
                ) {
                    vm.back()
                }
                if (!vm.isLogged) {
                    AuthScreen(onAuthSuccess = vm::onAuthSuccess)
                } else {
                    when (vm.currentScreen) {
                        Screen.HOME -> HomeScreen(
                            username = vm.username,
                            onNavigate = vm::navigate
                        )

                        Screen.CASES -> CasesScreen(
                            cases = vm.cases,
                            isLoading = vm.casesLoading,
                            errorMessage = vm.casesError,
                            onLoad = { vm.loadCases() },
                            onNavigate = vm::navigate,
                            onCaseSelected = vm::selectCase
                        )

                        Screen.CASE_DETAILS -> vm.selectedCase?.let { case ->
                            CaseDetailsScreen(
                                case = case,
                                onNavigate = vm::navigate,
                                onStartCase = {
                                    vm.navigate(Screen.SOLUTION)
                                }
                            )
                        }

                        Screen.SOLUTION -> vm.selectedCase?.let { case ->
                            SolutionScreen(
                                case = case,
                                isLoading = vm.submitLoading,
                                errorMessage = vm.submitError,
                                onSubmit = vm::submitSolution,
                                onNavigate = vm::navigate
                            )
                        }

                        Screen.RESULT -> vm.caseResult?.let { result ->
                            ResultScreen(
                                result = result,
                                referenceSolution = vm.referenceText,
                                showReference = vm.showReference,
                                isReferenceLoading = vm.referenceLoading,
                                errorMessage = vm.referenceError,
                                onLoadReference = vm::loadReference,
                                onDismissReference = vm::dismissReference,
                                onNavigate = vm::navigate,

                                showRatingDialog = vm.showRatingDialog,
                                isRatingLoading = vm.ratingLoading,
                                ratingMessage = vm.ratingMessage,
                                onRateCase = vm::rateCase,
                                onDismissRating = vm::dismissRatingDialog
                            )
                        }

                        Screen.RATING -> RatingScreen(
                            topUsers = vm.topUsers,
                            isLoading = vm.topRatingLoading,
                            errorMessage = vm.ratingError,
                            onLoad = vm::loadRating,
                            onNavigate = vm::navigate
                        )

                        Screen.PROFILE -> ProfileScreen(
                            username = vm.username,
                            email = vm.email,
                            casesSolved = vm.casesSolved,
                            averageScore = vm.averageScore,
                            isLoading = vm.profileLoading,
                            errorMessage = vm.profileError,
                            onLoad = vm::loadProfileStats,
                            onNavigate = vm::navigate,
                            onLogout = vm::logout
                        )

                        Screen.GUIDE -> GuideScreen(
                            onNavigate = vm::navigate
                        )
                    }
                }
            }
        }
    }
}
