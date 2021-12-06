package com.kbd.cockfit;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.kbd.cockfit.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void registerActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login_button_register), withText("회원가입"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_email),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText.perform(click());

        ViewInteraction textInputEditText2 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_email),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("mmw1026@naver.com"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText3 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_pw),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("ansalsdn"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText4 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_pw2),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("ansalsdn"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText5 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_nic),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("Minuoo"), closeSoftKeyboard());

        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.register_layout_const),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction textInputEditText6 = onView(
                allOf(withText("mmw1026@naver.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_email),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(replaceText(""));

        ViewInteraction textInputEditText7 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_email),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText7.perform(closeSoftKeyboard());

        ViewInteraction constraintLayout2 = onView(
                allOf(withId(R.id.register_layout_const),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout2.perform(click());

        ViewInteraction textInputEditText8 = onView(
                allOf(withText("ansalsdn"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_pw),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText8.perform(replaceText(""));

        ViewInteraction textInputEditText9 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_pw),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText9.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText10 = onView(
                allOf(withText("ansalsdn"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_pw2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText10.perform(replaceText(""));

        ViewInteraction textInputEditText11 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_pw2),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText11.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText12 = onView(
                allOf(withText("Minuoo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_nic),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText12.perform(replaceText(""));

        ViewInteraction textInputEditText13 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_nic),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText13.perform(closeSoftKeyboard());

        ViewInteraction constraintLayout3 = onView(
                allOf(withId(R.id.register_layout_const),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout3.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.register_icon), withContentDescription("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        pressBack();

        ViewInteraction textInputEditText14 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_nic),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText14.perform(replaceText("Minuoo"), closeSoftKeyboard());

        ViewInteraction constraintLayout4 = onView(
                allOf(withId(R.id.register_layout_const),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout4.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.register_icon), withContentDescription("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textInputEditText15 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_email),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText15.perform(replaceText("mmw1026@naver.com"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.register_icon), withContentDescription("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        pressBack();

        ViewInteraction textInputEditText16 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_pw),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText16.perform(replaceText("ansalsdn"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.register_icon), withContentDescription("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView4.perform(click());

        pressBack();

        ViewInteraction textInputEditText17 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_pw2),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText17.perform(replaceText("ans"), closeSoftKeyboard());

        pressBack();

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.register_icon), withContentDescription("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView5.perform(click());

        ViewInteraction textInputEditText18 = onView(
                allOf(withText("ans"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_pw2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText18.perform(click());

        ViewInteraction textInputEditText19 = onView(
                allOf(withText("ans"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_pw2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText19.perform(replaceText("ansalsdn"));

        ViewInteraction textInputEditText20 = onView(
                allOf(withText("ansalsdn"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_pw2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText20.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText21 = onView(
                allOf(withText("Minuoo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_editText_nic),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText21.perform(replaceText(""));

        ViewInteraction textInputEditText22 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_nic),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText22.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction actionMenuItemView6 = onView(
                allOf(withId(R.id.register_icon), withContentDescription("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView6.perform(click());

        ViewInteraction textInputEditText23 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_nic),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText23.perform(click());

        ViewInteraction textInputEditText24 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.register_editText_nic),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText24.perform(replaceText("Minu"), closeSoftKeyboard());

        pressBack();

        ViewInteraction actionMenuItemView7 = onView(
                allOf(withId(R.id.register_icon), withContentDescription("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.register_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView7.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText25 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.login_editText_id),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText25.perform(click());

        ViewInteraction textInputEditText26 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.login_editText_id),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText26.perform(replaceText("mmw1026@naver.com"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText27 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.login_editText_pw),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText27.perform(replaceText("ansalsdn"), closeSoftKeyboard());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.login_button_login), withText("로그인"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
