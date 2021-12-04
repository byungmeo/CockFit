package com.kbd.cockfit;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
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
public class MakeRecipeActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA");

    @Test
    public void makeRecipeActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction hb = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.btn_google),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        hb.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.page_myRecipe), withContentDescription("My레시피"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.myRecipe_recyclerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

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
                                withId(R.id.make_editText_name),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("Vacation"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText2 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_proof),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("50"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText3 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_base),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("Vodka"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText4 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_ingredient),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("Travel"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText5 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_equipment),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("Car"), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText6 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_tags),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText6.perform(replaceText("Nice"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatEditText = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_description),
                                0),
                        0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Really Want.."), closeSoftKeyboard());

        pressBack();

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withText("Vacation"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_name),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(replaceText(""));

        ViewInteraction textInputEditText8 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_name),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText8.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textInputEditText9 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_name),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText9.perform(replaceText("Vacation"), closeSoftKeyboard());

        ViewInteraction textInputEditText10 = onView(
                allOf(withText("50"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_proof),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText10.perform(replaceText(""));

        ViewInteraction textInputEditText11 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_proof),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText11.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction textInputEditText12 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_proof),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText12.perform(replaceText("50"), closeSoftKeyboard());

        ViewInteraction textInputEditText13 = onView(
                allOf(withText("Vodka"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_base),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText13.perform(replaceText(""));

        ViewInteraction textInputEditText14 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_base),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText14.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction textInputEditText15 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_base),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText15.perform(replaceText("Vodka"), closeSoftKeyboard());

        ViewInteraction textInputEditText16 = onView(
                allOf(withText("Travel"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_ingredient),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText16.perform(replaceText(""));

        ViewInteraction textInputEditText17 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_ingredient),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText17.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView5.perform(click());

        ViewInteraction textInputEditText18 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_ingredient),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText18.perform(replaceText("Travel"), closeSoftKeyboard());

        ViewInteraction textInputEditText19 = onView(
                allOf(withText("Car"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_equipment),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText19.perform(replaceText(""));

        ViewInteraction textInputEditText20 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_equipment),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText20.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView6 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView6.perform(click());

        ViewInteraction textInputEditText21 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_equipment),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText21.perform(replaceText("Car"), closeSoftKeyboard());

        ViewInteraction textInputEditText22 = onView(
                allOf(withText("Nice"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_tags),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText22.perform(replaceText(""));

        ViewInteraction textInputEditText23 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_tags),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText23.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView7 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView7.perform(click());

        ViewInteraction textInputEditText24 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_tags),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText24.perform(replaceText("Nice"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withText("Really Want.."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_description),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText(""));

        ViewInteraction appCompatEditText3 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_description),
                                0),
                        0),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView8 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView8.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.make_editText_description),
                                0),
                        0),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Really Want..."), closeSoftKeyboard());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.make_imageView_addImage),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0)));
        appCompatImageView.perform(scrollTo(), click());

        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button2), withText("앨범선택"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.make_imageView_addImage),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0)));
        appCompatImageView2.perform(scrollTo(), click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("사진촬영"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction actionMenuItemView9 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView9.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.myRecipeItem_imageButton_share),
                        childAtPosition(
                                allOf(withId(R.id.cardView_Recipe_Setting),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("공유"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.post_materialToolbar),
                                        childAtPosition(
                                                withId(R.id.post_appBarLayout),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.myRecipeItem_button_edit),
                        childAtPosition(
                                allOf(withId(R.id.cardView_Recipe_Setting),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText25 = onView(
                allOf(withText("50"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_proof),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText25.perform(replaceText("60"));

        ViewInteraction textInputEditText26 = onView(
                allOf(withText("60"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.make_editText_proof),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText26.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView10 = onView(
                allOf(withId(R.id.make_button_store), withContentDescription("storerecipe"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topAppBar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView10.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.myRecipeItem_button_delete),
                        childAtPosition(
                                allOf(withId(R.id.cardView_Recipe_Setting),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("확인"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());
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
