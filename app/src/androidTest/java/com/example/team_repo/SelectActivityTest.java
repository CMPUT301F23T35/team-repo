package com.example.team_repo;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
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
public class SelectActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void selectActivityTest(){
        // login
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

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.add), withContentDescription("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        // add first item
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.ItemName),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.DatePurchase),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Description),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.ItemMake),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.ItemModel),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.ItemSerial),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                0),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.EstimatedValue),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btn_confirm), withText("Confirm"),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        materialButton5.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }

        // add second item
        appCompatEditText4.perform(replaceText("2"), closeSoftKeyboard());
        appCompatEditText3.perform(click());
        materialButton4.perform(scrollTo(), click());
        appCompatEditText6.perform(replaceText("2"), closeSoftKeyboard());
        appCompatEditText7.perform(replaceText("2"), closeSoftKeyboard());
        appCompatEditText8.perform(replaceText("2"), closeSoftKeyboard());
        appCompatEditText9.perform(replaceText("2"), closeSoftKeyboard());
        appCompatEditText10.perform(replaceText("2"), closeSoftKeyboard());
        materialButton5.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.selectItemsButton), withText("Select"),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        appCompatButton.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.checkBox),
                        hasSibling(withText("1")),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.checkBox),
                        hasSibling(withText("2")),
                        isDisplayed()));
        materialCheckBox2.perform(click());


        ViewInteraction materialCheckBox4 = onView(
                allOf(withId(R.id.cb_tag),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.TagListView),
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox4.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.add_tags_button), withText("ADD TAG"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                3)),
                                1),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(childAtPosition(
                                allOf(withId(R.id.select_toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        pressBack();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        DataInteraction constraintLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.homepageListView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                6)))
                .atPosition(0);
        constraintLayout2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_item_tag_content), withText("tag1"),
                        withParent(withParent(withId(R.id.detail_tags_recycler_view))),
                        isDisplayed()));
        textView.check(matches(withText("tag1")));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(childAtPosition(
                                allOf(withId(R.id.item_toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.itemName), withText("1"),
                        withParent(withParent(withId(R.id.homepageListView))),
                        isDisplayed()));
        textView2.check(matches(withText("1")));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.selectItemsButton), withText("Select"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        3),
                                3),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(childAtPosition(
                                allOf(withId(R.id.select_toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

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