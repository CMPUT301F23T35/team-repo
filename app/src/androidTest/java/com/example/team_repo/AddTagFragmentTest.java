package com.example.team_repo;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddTagFragmentTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void addTagFragmentTest() {
        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.tv_jump_to_login), withText("Already have an account?"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                5),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.login_email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.login_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btn_login), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        try
        {
            Thread.sleep(4000);
        } catch(InterruptedException e) {}

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.tag), withContentDescription("Tag"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        try
        {
            Thread.sleep(2000);
        } catch(InterruptedException e) {}

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTag),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("tag1"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btn_addTag), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        1),
                                3),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_tag_content), withText("tag1"),
                        withParent(withParent(withId(R.id.tagRecyclerView))),
                        isDisplayed()));
        textView.check(matches(withText("tag1")));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.btn_tag_delete),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tagRecyclerView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        try
        {
            Thread.sleep(2000);
        } catch(InterruptedException e) {}
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
