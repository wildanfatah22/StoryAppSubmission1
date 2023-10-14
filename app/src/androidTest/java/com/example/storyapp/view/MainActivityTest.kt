package com.example.storyapp.view

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.storyapp.helper.EspressoIdlingResource
import com.example.storyapp.R
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var activityScenario: ActivityScenario<LoginActivity>

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
        activityScenario = activityRule.scenario
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loginAndLogout() {
        // Login Test
        Espresso.onView(withId(R.id.edt_email_input))
            .perform(ViewActions.typeText("awikwok@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.edt_password_input))
            .perform(ViewActions.typeText("awikwokk"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btn_login)).perform(ViewActions.click())

        // Verify loading indicator is shown
        Espresso.onView(withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Wait for API response with idling resource
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        // redirect to HomePageActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Verify user is logged in with checking displayed data
        Espresso.onView(withId(R.id.rv_stories))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.rv_stories)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )

        // Logout Test
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        Espresso.onView(withText(R.string.logout)).perform(ViewActions.click())
        Espresso.onView(withText(R.string.logout_description))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withText(R.string.yes)).perform(ViewActions.click())

        // Verify user is logged out
        Espresso.onView(withId(R.id.tv_login))
            .check(ViewAssertions.matches(withText(R.string.tvLogin)))
    }
}