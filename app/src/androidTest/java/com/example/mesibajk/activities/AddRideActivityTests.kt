package com.example.mesibajk.activities


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.mesibajk.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddRideActivityTests {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AddRideActivity::class.java)

    @Test
    fun testUI() {
        onView(withId(R.id.izberi_kolo_title)).check(matches(withText(R.string.izberi_kolo)))
        onView(withId(R.id.izberi_oddelek_title)).check(matches(withText(R.string.izberi_svoj_oddelek)))
        onView(withId(R.id.izberi_razdaljo_title)).check(matches(withText(R.string.oznaci_razdaljo)))
        onView(withId(R.id.spinner_bike)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.spinner_department)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.spinner_purpose)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.seek_bar_distance)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.add_ride_btn)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun emptyNameFieldErrorTest() {
        onView(withId(R.id.add_ride_btn)).perform(scrollTo(), click())
        onView(withText("Polje je obvezno!")).check(matches(isDisplayed()))
    }
}
