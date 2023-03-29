package com.sam.kladishoes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.sam.kladishoes.presentation.accountScreen.AccountScreen
import com.sam.kladishoes.presentation.cartScreen.CartScreen
import com.sam.kladishoes.presentation.cartScreen.CartScreenViewModel
import com.sam.kladishoes.presentation.cartScreen.PurchaseDialog
import com.sam.kladishoes.presentation.orderScreen.OrderScreen
import com.sam.kladishoes.presentation.registration.login.LoginScreen
import com.sam.kladishoes.presentation.registration.signUp.SignUpScreen
import com.sam.kladishoes.presentation.shoedetailscreen.ShoeDetailScreen
import com.sam.kladishoes.presentation.shoelistscreen.ShoeListScreen
import com.sam.kladishoes.presentation.shoelistscreen.ShoeListViewModel
import com.sam.kladishoes.ui.theme.KladiShoesTheme
import com.sam.kladishoes.utils.ROUTES
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KladiShoesTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(viewModel: MainViewModel = hiltViewModel()) {

    val user = viewModel.authenticationService.user
    val startRoute = if (user != null) {
        ROUTES.HOME
    } else {
        ROUTES.AUTHENTICATION
    }
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        authentication(navController = navController) {
            navController.navigate(ROUTES.HOME) {
                popUpTo(
                    navController.graph.id
                ) {
                    inclusive = true
                }
            }
        }
        home(navController = navController) {
            navController.navigate(ROUTES.AUTHENTICATION) {
                popUpTo(
                    navController.graph.id
                ) {
                    inclusive = true
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.home(navController: NavController, signOut: () -> Unit) {
    navigation(ROUTES.SHOE_LIST, route = ROUTES.HOME) {
        composable(
            route = ROUTES.SHOE_LIST,
            exitTransition = {
                when (targetState.destination.route) {
                    ROUTES.SHOE_ITEM -> {
                        slideOutHorizontally(
                            targetOffsetX = { -500 },
                            animationSpec = tween(500)
                        ) + fadeOut(animationSpec = tween(700))
                    }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    ROUTES.SHOE_ITEM -> {
                        slideInHorizontally(
                            initialOffsetX = { -500 },
                            animationSpec = tween(500)
                        ) + fadeIn(animationSpec = tween(700))
                    }
                    else -> null
                }
            }
        ) {
            ShoeListScreen(
                onCartClicked = {
                    navController.navigate(ROUTES.CART_SCREEN)
                },
                onShoeClicked = { shoeId ->
                    navController.navigate(ROUTES.SHOE_ITEM + "?shoeId=$shoeId")
                },
                signOut = signOut,
                onOrderClicked = {
                    navController.navigate(ROUTES.ORDERS)
                },
                onAccountClicked = {
                    navController.navigate(ROUTES.ACCOUNT)
                }
            )
        }
        composable(
            route = ROUTES.SHOE_ITEM + "?shoeId={shoeId}",
            arguments = listOf(
                navArgument("shoeId") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    ROUTES.SHOE_LIST -> {
                        slideInHorizontally(
                            initialOffsetX = { 500 },
                            animationSpec = tween(500)
                        ) + fadeIn(animationSpec = tween(700))
                    }
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    ROUTES.SHOE_LIST -> {
                        slideOutHorizontally(
                            targetOffsetX = { 500 },
                            animationSpec = tween(500)
                        ) + fadeOut(animationSpec = tween(700))
                    }
                    else -> null
                }
            }
        ) {
            ShoeDetailScreen {
                navController.popBackStack()
            }
        }
        composable(ROUTES.ACCOUNT) {
            val viewModel: ShoeListViewModel = hiltViewModel()
            val email = viewModel.email
            AccountScreen (email = email){
                viewModel.signOut()
                navController.navigate(ROUTES.AUTHENTICATION) {
                    popUpTo(ROUTES.HOME) {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = ROUTES.CART_SCREEN
        ) {
            CartScreen(
                onCheckOutClicked = {
                    navController.navigate(ROUTES.DIALOG + "?amount=$it")
                }
            ) {
                navController.popBackStack()
            }
        }
        composable(route = ROUTES.ORDERS) {
            OrderScreen() {
                navController.popBackStack()
            }
        }
        dialog(
            route = ROUTES.DIALOG + "?amount={amount}",
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                }
            )
        ) { navBackStack ->
            val totalAmount = navBackStack.arguments?.getString("amount")!!
            val viewModel: CartScreenViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState()
            PurchaseDialog(
                totalAmount = totalAmount,
                state = state.value,
                onCheckOutClicked = { phoneNumber, totalPrice ->
                    viewModel.checkOut(phoneNumber, totalPrice.toDouble()) {
                        navController.popBackStack()
                    }
                }
            ) {
                navController.popBackStack()
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authentication(navController: NavController, navigate: () -> Unit) {
    navigation(ROUTES.SIGN_UP, route = ROUTES.AUTHENTICATION) {
        composable(route = ROUTES.SIGN_UP,
            exitTransition = {
                if (targetState.destination.route == ROUTES.LOG_IN) {
                    slideOutHorizontally(
                        targetOffsetX = { -700 },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == ROUTES.LOG_IN) {
                    slideInHorizontally(
                        initialOffsetX = { -700 },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                } else {
                    null
                }
            }
        ) {
            SignUpScreen(
                onLogInClicked = {
                    navController.navigate(ROUTES.LOG_IN)
                },
                onNavigate = {
                    navigate()
                }
            )
        }
        composable(
            route = ROUTES.LOG_IN,
            enterTransition = {
                if (initialState.destination.route == ROUTES.SIGN_UP) {
                    slideInHorizontally(
                        initialOffsetX = { 700 },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                } else {
                    null
                }
            },
            popExitTransition = {
                if (targetState.destination.route == ROUTES.SIGN_UP) {
                    slideOutHorizontally(
                        targetOffsetX = { 700 },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                } else {
                    null
                }
            }
        ) {
            LoginScreen(
                onBackClicked = { navController.popBackStack() },
                navigate = {
                    navigate()
                }
            )
        }
    }
}