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
public class CommunityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void communityTest() {
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
                allOf(withId(R.id.page_community), withContentDescription("커뮤니티"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.forumItem_textView_more), withText("더보기"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.cardview.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.forum_menuItem_wirtePost), withText("글작성"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.forum_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.write_editText_title_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.write_editText_title_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("Hi Hello"), closeSoftKeyboard());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.write_editText_content_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Hi Hello"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.write_menuItem_write), withText("게시"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.forum_recycler),
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

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.general_button_like), withText("0"),
                        childAtPosition(
                                allOf(withId(R.id.general_constraintLayout_contents),
                                        childAtPosition(
                                                withId(R.id.general_constraintLayout),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.post_editText_comment),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.post_editText_comment),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.post_button_comment), withText("작성"),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.comment_imageButton_more),
                        childAtPosition(
                                allOf(withId(R.id.comment_linearLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.commentDialog_button_delete), withText("댓글 삭제하기"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.post_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.title), withText("글 수정"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.write_editText_title_text), withText("Hi Hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText3.perform(click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.write_editText_title_text), withText("Hi Hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("Hi Hello Everyone"));

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.write_editText_title_text), withText("Hi Hello Everyone"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.write_editText_content_text), withText("Hi Hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Hi Hello Everyone"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.write_editText_content_text), withText("Hi Hello Everyone"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.write_menuItem_write), withText("게시"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.general_button_like), withText("1"),
                        childAtPosition(
                                allOf(withId(R.id.general_constraintLayout_contents),
                                        childAtPosition(
                                                withId(R.id.general_constraintLayout),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction overflowMenuButton2 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.post_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        overflowMenuButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.title), withText("글 삭제"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.forum_materialToolbar),
                                childAtPosition(
                                        withId(R.id.forum_appBarLayout),
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

        ViewInteraction materialTextView4 = onView(
                allOf(withId(R.id.forumItem_textView_more), withText("더보기"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.cardview.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.forum_menuItem_wirtePost), withText("글 작성"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.forum_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.write_editText_title_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.write_editText_title_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(replaceText("Hi"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.write_editText_content_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("Hi"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.write_menuItem_write), withText("게시"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.forum_recycler),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.general_button_like), withText("0"),
                        childAtPosition(
                                allOf(withId(R.id.general_constraintLayout_contents),
                                        childAtPosition(
                                                withId(R.id.general_constraintLayout),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.post_editText_comment),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.post_editText_comment),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.post_button_comment), withText("작성"),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.comment_imageButton_more),
                        childAtPosition(
                                allOf(withId(R.id.comment_linearLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.commentDialog_button_delete), withText("댓글 삭제하기"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        materialButton7.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction overflowMenuButton3 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.post_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        overflowMenuButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialTextView5 = onView(
                allOf(withId(R.id.title), withText("글 수정"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.write_editText_title_text), withText("Hi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText8.perform(click());

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.write_editText_title_text), withText("Hi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText9.perform(replaceText("Hi Hello"));

        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.write_editText_title_text), withText("Hi Hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_title),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText10.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.write_editText_content_text), withText("Hi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("Hi Hello"));

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.write_editText_content_text), withText("Hi Hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_editText_content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        ViewInteraction actionMenuItemView6 = onView(
                allOf(withId(R.id.write_menuItem_write), withText("게시"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.write_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView6.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.general_button_like), withText("1"),
                        childAtPosition(
                                allOf(withId(R.id.general_constraintLayout_contents),
                                        childAtPosition(
                                                withId(R.id.general_constraintLayout),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton8.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction overflowMenuButton4 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.post_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        overflowMenuButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialTextView6 = onView(
                allOf(withId(R.id.title), withText("글 삭제"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView6.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton4 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.forum_materialToolbar),
                                childAtPosition(
                                        withId(R.id.forum_appBarLayout),
                                        0)),
                        1),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.page_myRecipe), withContentDescription("My레시피"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.myRecipeItem_imageButton_share),
                        childAtPosition(
                                allOf(withId(R.id.cardView_Recipe_Setting),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(android.R.id.button1), withText("공유"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton9.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.share_button_like), withText("0"),
                        childAtPosition(
                                allOf(withId(R.id.share_constraintLayout_recipeInfo),
                                        childAtPosition(
                                                withId(R.id.share_linearLayout),
                                                0)),
                                12),
                        isDisplayed()));
        materialButton10.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.post_editText_comment),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText11.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.post_editText_comment),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("tst"), closeSoftKeyboard());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.post_button_comment), withText("작성"),
                        childAtPosition(
                                allOf(withId(R.id.post_constraint_comment),
                                        childAtPosition(
                                                withId(R.id.post_cardView_comment),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withId(R.id.comment_imageButton_more),
                        childAtPosition(
                                allOf(withId(R.id.comment_linearLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                1),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.commentDialog_button_delete), withText("댓글 삭제하기"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.share_button_like), withText("1"),
                        childAtPosition(
                                allOf(withId(R.id.share_constraintLayout_recipeInfo),
                                        childAtPosition(
                                                withId(R.id.share_linearLayout),
                                                0)),
                                12),
                        isDisplayed()));
        materialButton13.perform(click());

        ViewInteraction materialButton14 = onView(
                allOf(withId(R.id.share_button_bookmark), withText("즐찾"),
                        childAtPosition(
                                allOf(withId(R.id.share_constraintLayout_recipeInfo),
                                        childAtPosition(
                                                withId(R.id.share_linearLayout),
                                                0)),
                                13),
                        isDisplayed()));
        materialButton14.perform(click());

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.share_button_bookmark), withText("즐찾"),
                        childAtPosition(
                                allOf(withId(R.id.share_constraintLayout_recipeInfo),
                                        childAtPosition(
                                                withId(R.id.share_linearLayout),
                                                0)),
                                13),
                        isDisplayed()));
        materialButton15.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction overflowMenuButton5 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.post_materialToolbar),
                                        2),
                                0),
                        isDisplayed()));
        overflowMenuButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialTextView7 = onView(
                allOf(withId(R.id.title), withText("글 삭제"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView7.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.page_community), withContentDescription("커뮤니티"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());
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
