package rocket.style.test.stylehelper.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rocket.dom.client.DomHelper;
import rocket.style.client.CssUnit;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.style.client.support.StyleSupportConstants;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;
import rocket.util.client.Colour;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A series of automated tests that occasionally prompts the user to confirm some visual changes.
 * @author Miroslav Pokorny (mP)
 */
public class StyleHelperTest extends WebPageTestRunner implements EntryPoint{

    final int WIDTH = 400;
    final int HEIGHT = 50;
    final String FILTER = "filter";

    public void onModuleLoad(){
        final Button button = new Button("Run Tests");
        button.addClickListener( new ClickListener(){
            public void onClick( final Widget sender ){
                StyleHelperTest.this.executeTests( new TestBuilder(){
                    public List buildCandidates(){
                        return StyleHelperTest.this.buildCandidates();
                    }
                });
            }
        });
        RootPanel.get().add( button );

        button.setFocus( true );
    }

    protected List buildCandidates() {
        final List tests = new ArrayList();

        tests.add(new Test() {
            public String getName(){
                return "testConvertPixelsToPixels";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPixelsToPixels();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertInchesToPixels";
            }
            public void execute() {
                StyleHelperTest.this.testConvertInchesToPixels();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertPixelsToInches";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPixelsToInches();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertCentimetersToPixels";
            }
            public void execute() {
                StyleHelperTest.this.testConvertCentimetersToPixels();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertPixelsToCentimeters";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPixelsToCentimeters();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertMillimetersToPixels";
            }
            public void execute() {
                StyleHelperTest.this.testConvertMillimetersToPixels();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertPixelsToMillimeters";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPixelsToMillimeters();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertPointsToPixels";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPointsToPixels();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertPixelsToPoints";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPixelsToPoints();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertPicasToPixels";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPicasToPixels();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testConvertPixelsToPicas";
            }
            public void execute() {
                StyleHelperTest.this.testConvertPixelsToPicas();
            }
        });
        tests.add( new Test() {
            public String getName(){
                return "testGetInlineStylePropertyValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetInlineStylePropertyValue();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "testSetInlineStylePropertyValue";
            }
            public void execute() {
                StyleHelperTest.this.testSetInlineStylePropertyValue();
            }
        });
        tests.add( new Test() {
            public String getName(){
                return "testGetComputedPropertyValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedPropertyValue();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetInlineOpacity";
            }
            public void execute() {
                StyleHelperTest.this.testGetInlineOpacity();
            }
        });
        tests.add( new Test() {
            public String getName(){
                return "testGetComputedOpacity";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedOpacity();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testSetInlineOpacity";
            }
            public void execute() {
                StyleHelperTest.this.testSetInlineOpacity();
            }
        });

        tests.add( new Test() {

            public String getName(){
                return "testGetComputedWidthWhereDivHasInlineWidthAndNoBorderOrPadding";
            }
            public void execute() {                
                StyleHelperTest.this.testGetComputedWidthWhereDivHasInlineWidthAndNoBorderOrPadding();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedWidthWhereDivInheritsBorderPaddingWidthFromParent0";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedWidthWhereDivInheritsBorderPaddingWidthFromParent0();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedWidthWhereDivInheritsBorderPaddingWidthFromParent1";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedWidthWhereDivInheritsBorderPaddingWidthFromParent1();
            }
        });

//      tests.add("testGetComputedWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent", new Test() {
//      public String getName(){
//      return "testGetComputedWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent";
//      }
//      public void execute() {
//      StyleHelperSupportTest.this.testGetComputedWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent();
//      }
//      });

        tests.add(new Test() {
            public String getName(){
                return "testGetComputedHeightWhereDivHasInlineHeightAndNoBorderOrPadding";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedHeightWhereDivHasInlineHeightAndNoBorderOrPadding();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedHeightWhereDivInheritsBorderPaddingHeightFromParent0";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedHeightWhereDivInheritsBorderPaddingHeightFromParent0();
            }
        });

        tests.add(new Test() {
            public String getName(){
                return "testGetComputedHeightWhereDivInheritsBorderPaddingHeightFromParent1";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedHeightWhereDivInheritsBorderPaddingHeightFromParent1();
            }
        });

//      tests.add( new Test() {
//      public String getName(){
//      return "testGetComputedHeightWhereDivHasScrollBarsAndInheritsBorderPaddingHeightFromParent";
//      }
//      public void execute() {
//      StyleHelperSupportTest.this.testGetComputedHeightWhereDivHasScrollBarsAndInheritsBorderPaddingHeightFromParent();
//      }
//      });      

        tests.add( new Test() {
            public String getName(){
                return "testSetInlineBackgroundImage";
            }
            public void execute() {
                StyleHelperTest.this.testSetInlineBackgroundImage();
            }
        });
        tests.add( new Test() {
            public String getName(){
                return "testSetInlineBackgroundImageWithElementAlsoContainingABackgroundColour";
            }
            public void execute() {
                StyleHelperTest.this.testSetInlineBackgroundImageWithElementAlsoContainingABackgroundColour();
            }
        });


        tests.add( new Test() {
            public String getName(){
                return "testGetComputedBackgroundPositionWhenNoValueIsSet";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedBackgroundPositionWhenNoValueIsSet();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedBackgroundPosition";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedBackgroundPosition();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedBackgroundPositionWithElementThatIncludesAllTheOtherBackgroundProperties";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedBackgroundPositionWithElementThatIncludesAllTheOtherBackgroundProperties();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToXSmallValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToXSmallValue();
            }
        });


        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToSmallValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToSmallValue();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToMediumValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToMediumValue();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToLargeValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToLargeValue();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToXLargeValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToXLargeValue();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToXXLargeValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToXXLargeValue();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToSmaller";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToSmaller();
            }
        });


        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontSizeSetToLarger";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontSizeSetToLarger();
            }
        });

        
        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontWeightWithMissingPropertyValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontWeightWithMissingPropertyValue();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontWeightWithNumberPropertyValue";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontWeightWithNumberPropertyValue();
            }
        });
        
        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontWeightSetToNormal";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontWeightSetToNormal();
            }
        });        

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontWeightSetToBold";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontWeightSetToBold();
            }
        });
        

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontWeightSetToLighter";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontWeightSetToLighter();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedFontWeightSetToBolder";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedFontWeightSetToBolder();
            }
        });
        
        tests.add( new Test() {
            public String getName(){
                return "testGetComputedBorderWidthThin";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedBorderWidthThin();
            }
        });
        tests.add( new Test() {
            public String getName(){
                return "testGetComputedBorderWidthMedium";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedBorderWidthMedium();
            }
        });
        tests.add( new Test() {
            public String getName(){
                return "testGetComputedBorderWidthThick";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedBorderWidthThick();
            }
        });

        tests.add( new Test() {
            public String getName(){
                return "testGetComputedOpacity";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedBorderWidthThick();
            }
        });

        tests.add(new Test() {
            public String getName(){
                return "getComputedStylePropertyNames0";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedStylePropertyNames0();
            }
        });
        tests.add(new Test() {
            public String getName(){
                return "getComputedStylePropertyNames1";
            }
            public void execute() {
                StyleHelperTest.this.testGetComputedStylePropertyNames1();
            }
        });

        return tests;

    }

    protected void testGetInlineStylePropertyValue() {
        final Element element = this.createDivAndAddToDocument();
        final String propertyName = StyleConstants.BACKGROUND_COLOR;
        final String propertyValue = Colour.getColour("yellow").toCssColour();
        DOM.setStyleAttribute(element, propertyName, propertyValue);
        StyleHelper.setInlineStyleProperty(element, StyleConstants.WIDTH, WIDTH + "px");
        StyleHelper.setInlineStyleProperty(element, StyleConstants.HEIGHT, HEIGHT + "px");

        String actualPropertyValue = StyleHelper.getInlineStyleProperty(element, propertyName);
        actualPropertyValue = Colour.parse( actualPropertyValue ).toCssColour();
        final String expectedPropertyValue = propertyValue;
        Test.assertEquals(expectedPropertyValue, actualPropertyValue);
    }

    protected void testSetInlineStylePropertyValue() {
        final Element element = this.createDivAndAddToDocument();
        final String propertyValue = Colour.getColour("aquamarine").toCssColour();

        StyleHelper.setInlineStyleProperty(element, StyleConstants.BACKGROUND_COLOR, propertyValue);
        StyleHelper.setInlineStyleProperty(element, StyleConstants.WIDTH, WIDTH + "px");
        StyleHelper.setInlineStyleProperty(element, StyleConstants.HEIGHT, HEIGHT + "px");       

        this.scrollIntoView(element);        
        TestRunner.postponeCurrentTest( 60 * 1000 );

        DeferredCommand.add( new Command(){
            public void execute(){
                if (false == Window.confirm("Has the background colour of the last element changed to aquamarine ?")) {
                    Test.fail("The background colour did not change.");
                }
                TestRunner.finishTest();
            }
        });
    }    

    protected void testGetComputedPropertyValue() {
        final Element element = this.createDivAndAddToDocument();
        final String propertyName = StyleConstants.BACKGROUND_COLOR;
        final String propertyValue = Colour.getColour("yellow").toCssColour();
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        String actualPropertyValue = StyleHelper.getInlineStyleProperty(element, propertyName);
        actualPropertyValue = Colour.parse( actualPropertyValue ).toCssColour();
        final String expectedPropertyValue = propertyValue;
        Test.assertEquals(expectedPropertyValue, actualPropertyValue);
    }

    protected void testGetInlineOpacity() {
        final Element containerElement = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( containerElement, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.BACKGROUND_COLOR, "yellow");

        final Element childElement = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( childElement, StyleConstants.POSITION, "relative");
        DOM.setStyleAttribute( childElement, StyleConstants.LEFT, - WIDTH /2 +"px");
        DOM.setStyleAttribute( childElement, StyleConstants.TOP, - HEIGHT /2 +"px");
        DOM.setStyleAttribute( childElement, StyleConstants.Z_INDEX, "1");
        DOM.setStyleAttribute( childElement, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( childElement, StyleConstants.BACKGROUND_COLOR, "blue");

        final float opacity = 0.5f;
        DOM.setStyleAttribute( childElement, FILTER, "alpha(opacity=" + (int)(opacity*100) + ")");
        DOM.setStyleAttribute( childElement, StyleConstants.OPACITY, "" + opacity);

        final String actualOpacity = StyleHelper.getInlineStyleProperty( childElement, StyleConstants.OPACITY );
        final String expectedOpacity = "" + opacity;
        Test.assertEquals( "actualOpacity: " + actualOpacity + ", expectedOpacity: " + expectedOpacity, Double.parseDouble(expectedOpacity ), Double.parseDouble( actualOpacity), 0.5);
    }

    protected void testGetComputedOpacity() {
        final Element containerElement = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( containerElement, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.BACKGROUND_COLOR, "yellow");

        final Element childElement = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( childElement, StyleConstants.POSITION, "relative");
        DOM.setStyleAttribute( childElement, StyleConstants.LEFT, -WIDTH / 2 + "px");
        DOM.setStyleAttribute( childElement, StyleConstants.TOP, - HEIGHT / 2 + "px");
        DOM.setStyleAttribute( childElement, StyleConstants.Z_INDEX, "1");
        DOM.setStyleAttribute( childElement, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( childElement, StyleConstants.BACKGROUND_COLOR, "blue");

        final float opacity = 0.5f;
        DOM.setStyleAttribute( childElement, FILTER, "alpha(opacity=" + (int)(opacity * 100 )+ ")");
        DOM.setStyleAttribute( childElement, StyleConstants.OPACITY, "" + opacity );

        final String actualOpacity = StyleHelper.getComputedStyleProperty( childElement, StyleConstants.OPACITY );
        final String expectedOpacity = "" + opacity;
        Test.assertEquals( "actualOpacity: " + actualOpacity + ", expectedOpacity: " + expectedOpacity, Double.parseDouble(expectedOpacity ), Double.parseDouble( actualOpacity), 0.5);
    }

    protected void testSetInlineOpacity() {
        final Element containerElement = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( containerElement, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.BACKGROUND_COLOR, "yellow");

        final Element childElement = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( childElement, StyleConstants.POSITION, "relative");
        DOM.setStyleAttribute( childElement, StyleConstants.LEFT, -WIDTH / 2 + "px");
        DOM.setStyleAttribute( childElement, StyleConstants.TOP, - HEIGHT / 2 + "px");
        DOM.setStyleAttribute( childElement, StyleConstants.Z_INDEX, "1");
        DOM.setStyleAttribute( childElement, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( childElement, StyleConstants.BACKGROUND_COLOR, "blue");

        this.scrollIntoView(childElement);        
        TestRunner.postponeCurrentTest( 60 * 1000 );

        DeferredCommand.add( new Command(){
            public void execute(){               
                if( false == Window.confirm("Does a blue rectangle overlap a yellow background ?" )){
                    Test.fail( "The blue rectangle is not blue.");
                }

                final float opacity = 0.5f;
                final String propertyName = StyleConstants.OPACITY;
                final String propertyValue = "" + opacity;
                StyleHelper.setInlineStyleProperty(childElement, propertyName, propertyValue);

                if( false == Window.confirm("Is the rectangle that was blue now green where it overlaps the yellow background ?") ){
                    Test.fail( "The rectangle overlaying the yellow rectangle is not green.");
                }

                TestRunner.finishTest();
            }
        });
    }

    protected void testGetComputedWidthWhereDivHasInlineWidthAndNoBorderOrPadding() {
        final int borderLeftWidth = 0;
        final int borderRightWidth = 0;
        final int paddingLeft = 0;
        final int paddingRight = 0;

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( element, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( element, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_WIDTH, "0px");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( element, StyleConstants.PADDING, "0px");
        DOM.setStyleAttribute( element, StyleConstants.PADDING_LEFT, paddingLeft + "px");
        DOM.setStyleAttribute( element, StyleConstants.PADDING_RIGHT, paddingRight + "px");
        DOM.setStyleAttribute( element, StyleConstants.MARGIN, "0px");

        final String actualContentWidth = StyleHelper.getComputedStyleProperty( element, StyleConstants.WIDTH );
        final String expectedContentWidth = ( WIDTH - borderLeftWidth - borderRightWidth - paddingLeft - paddingRight ) + "px";
        Test.assertEquals( expectedContentWidth, actualContentWidth );
    }

    protected void testGetComputedWidthWhereDivInheritsBorderPaddingWidthFromParent0() {
        final int borderLeftWidth = 0;
        final int borderRightWidth = 0;
        final int paddingLeft = 0;
        final int paddingRight = 0;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setInnerHTML( parent, "&nbsp;");
        DOM.setStyleAttribute( parent, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_LEFT, paddingLeft + "px");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_RIGHT, paddingRight + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_BOTTOM, 25 + "px");

        final Element child = DOM.createDiv();
        DOM.setStyleAttribute( child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
        DOM.appendChild( parent, child);

        final String actualContentWidth = StyleHelper.getComputedStyleProperty( child, StyleConstants.WIDTH );
        final String expectedContentWidth = WIDTH + "px";
        Test.assertEquals( expectedContentWidth, actualContentWidth );
    }

    protected void testGetComputedWidthWhereDivInheritsBorderPaddingWidthFromParent1() {
        final int borderLeftWidth = 11;
        final int borderRightWidth = 12;
        final int paddingLeft = 13;
        final int paddingRight = 14;
        final int marginLeft = 15;
        final int marginRight = 16;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setInnerHTML( parent, "&nbsp;");
        DOM.setStyleAttribute( parent, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_LEFT, paddingLeft + "px");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_RIGHT, paddingRight + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_LEFT, marginLeft + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_RIGHT, marginRight + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_BOTTOM, 25 + "px");

        final Element child = DOM.createDiv();
        DOM.setStyleAttribute( child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
        DOM.appendChild( parent, child);
        DOM.setInnerHTML( child, "&nbsp;");

        final String actualContentWidth = StyleHelper.getComputedStyleProperty( child, StyleConstants.WIDTH );
        final String expectedContentWidth = WIDTH + "px";
        Test.assertEquals( expectedContentWidth, actualContentWidth );
    }

    protected void testGetComputedWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent() {
        final int borderLeftWidth = 11;
        final int borderRightWidth = 12;
        final int paddingLeft = 13;
        final int paddingRight = 14;
        final int marginLeft = 15;
        final int marginRight = 16;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( parent, StyleConstants.WIDTH, WIDTH + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_LEFT, paddingLeft + "px");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_RIGHT, paddingRight + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_LEFT, marginLeft + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_RIGHT, marginRight + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_BOTTOM, 25 + "px");
        DOM.setStyleAttribute( parent, StyleConstants.OVERFLOW, "scroll");

        final Element child = DOM.createDiv();
        DOM.setStyleAttribute( child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
        DOM.appendChild( parent, child);
        DOM.setInnerHTML( child, "CHILDtestGetComputedWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent");

        final String actualContentWidth = StyleHelper.getComputedStyleProperty( child, StyleConstants.WIDTH );
        final String expectedContentWidth = WIDTH + "px";
        Test.assertEquals( expectedContentWidth, actualContentWidth );
    }

    protected void testGetComputedHeightWhereDivHasInlineHeightAndNoBorderOrPadding() {
        final int borderTopWidth = 0;
        final int borderBottomWidth = 0;
        final int paddingTop = 0;
        final int paddingBottom = 0;

        final Element element = this.createDivAndAddToDocument();
        DOM.setInnerHTML( element, "");
        DOM.setStyleAttribute( element, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( element, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( element, StyleConstants.PADDING_LEFT, paddingTop + "px");
        DOM.setStyleAttribute( element, StyleConstants.PADDING_RIGHT, paddingBottom + "px");
        DOM.setStyleAttribute( element, StyleConstants.MARGIN_BOTTOM, 25 + "px");

        final String actualContentHeight = StyleHelper.getComputedStyleProperty( element, StyleConstants.HEIGHT );
        final String expectedContentHeight = ( HEIGHT - borderTopWidth - borderBottomWidth - paddingTop - paddingBottom ) + "px";
        Test.assertEquals( expectedContentHeight, actualContentHeight );
    }

    protected void testGetComputedHeightWhereDivInheritsBorderPaddingHeightFromParent0() {
        final int height = 100;
        final int borderTopWidth = 0;
        final int borderBottomWidth = 0;
        final int paddingTop = 0;
        final int paddingBottom = 0;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setInnerHTML( parent, "");
        DOM.setStyleAttribute( parent, StyleConstants.HEIGHT, height + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_TOP, paddingTop + "px");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_BOTTOM, paddingBottom + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN, "0px");

        final Element child = DOM.createDiv();
        DOM.setStyleAttribute( child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
        DOM.setStyleAttribute( child, StyleConstants.MARGIN, "0px");
        DOM.setStyleAttribute( child, StyleConstants.BORDER_WIDTH, "0px");
        DOM.setStyleAttribute( child, StyleConstants.PADDING, "0px");
        DOM.setStyleAttribute( child, StyleConstants.HEIGHT, "100%");
        DOM.appendChild( parent, child);
        DOM.setInnerHTML( child, "CHILD");

        final String actualContentHeight = StyleHelper.getComputedStyleProperty( child, StyleConstants.HEIGHT );
        final String expectedContentHeight = height + "px";
        Test.assertEquals( expectedContentHeight, actualContentHeight );
    }

    protected void testGetComputedHeightWhereDivInheritsBorderPaddingHeightFromParent1() {
        final int height = 100;
        final int borderTopWidth = 11;
        final int borderBottomWidth = 12;
        final int paddingTop = 13;
        final int paddingBottom = 14;
        final int marginTop = 15;
        final int marginBottom = 16;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setInnerHTML( parent, "");
        DOM.setStyleAttribute( parent, StyleConstants.HEIGHT, height + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_TOP, paddingTop + "px");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_BOTTOM, paddingBottom + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_TOP, marginTop + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_BOTTOM, marginBottom + "px");

        final Element child = DOM.createDiv();
        DOM.setStyleAttribute( child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
        DOM.setStyleAttribute( child, StyleConstants.MARGIN, "0px");
        DOM.setStyleAttribute( child, StyleConstants.BORDER_WIDTH, "0px");
        DOM.setStyleAttribute( child, StyleConstants.PADDING, "0px");
        DOM.setStyleAttribute( child, StyleConstants.HEIGHT, "100%");
        DOM.appendChild( parent, child);
        DOM.setInnerHTML( child, "CHILD");

        final String actualContentHeight = StyleHelper.getComputedStyleProperty( child, StyleConstants.HEIGHT );
        final String expectedContentHeight = height + "px";
        Test.assertEquals( expectedContentHeight, actualContentHeight );
    }

    protected void testGetComputedHeightWhereDivHasScrollBarsAndInheritsBorderPaddingHeightFromParent() {
        final int borderTopWidth = 11;
        final int borderBottomWidth = 12;
        final int paddingTop = 13;
        final int paddingBottom = 14;
        final int marginLeft = 15;
        final int marginRight = 16;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setInnerHTML( parent, "");
        DOM.setStyleAttribute( parent, StyleConstants.HEIGHT, HEIGHT + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_STYLE, "solid");
        DOM.setStyleAttribute( parent, StyleConstants.BORDER_COLOR, "lawnGreen");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_LEFT, paddingTop + "px");
        DOM.setStyleAttribute( parent, StyleConstants.PADDING_RIGHT, paddingBottom + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_LEFT, marginLeft + "px");
        DOM.setStyleAttribute( parent, StyleConstants.MARGIN_RIGHT, marginRight + "px");
        DOM.setStyleAttribute( parent, StyleConstants.OVERFLOW, "scroll");

        final Element child = DOM.createDiv();
        DOM.setStyleAttribute( child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
        DOM.appendChild( parent, child);
        DOM.setInnerHTML( child, "CHILDtestGetComputedHeightWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent");

        final String actualContentHeight = StyleHelper.getComputedStyleProperty( child, StyleConstants.HEIGHT );
        final String expectedContentHeight = HEIGHT + "px";
        Test.assertEquals( expectedContentHeight, actualContentHeight );
    }

    protected void testConvertPixelsToPixels(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "px", CssUnit.PX );
        final float expected = input;
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertInchesToPixels(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "in", CssUnit.PX );
        final float expected = Math.round( input / 96f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertPixelsToInches(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "px", CssUnit.IN );
        final float expected = Math.round( input * 96f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertCentimetersToPixels(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "cm", CssUnit.PX );
        final float expected = Math.round( input / 96f * 2.54f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertPixelsToCentimeters(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "px", CssUnit.CM );
        final float expected = Math.round( input * 96f / 2.54f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertMillimetersToPixels(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "mm", CssUnit.PX );
        final float expected = Math.round( input / 96f * 25.4f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertPixelsToMillimeters(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "px", CssUnit.MM );
        final float expected = Math.round( input * 96f / 25.4f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertPointsToPixels(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "pt", CssUnit.PX );
        final float expected = Math.round( input * 96f / 72f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertPixelsToPoints(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "px", CssUnit.PT );
        final float expected = Math.round( input / 96f * 72f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertPicasToPixels(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "pc", CssUnit.PX );
        final float expected = Math.round( input * 96f / 72f * 12f );
        Test.assertEquals(expected, actual, 0.75f);
    }
    protected void testConvertPixelsToPicas(){
        final float input = 1234;
        final float actual = StyleHelper.convertValue( input + "px", CssUnit.PC );
        final float expected = Math.round( input / 96f * 72f / 12f );
        Test.assertEquals(expected, actual, 0.75f);
    }    

    public void testSetInlineBackgroundImage() {
        final String propertyName = StyleConstants.BACKGROUND_IMAGE;
        final String propertyValue = "image.gif";

        final Element element = this.createDivAndAddToDocument();

        StyleHelper.setInlineStyleProperty( element, propertyName, "url('" + propertyValue + "')");

        // check that the style got updated...
        final String expected = propertyValue;
        final String actual = DOM.getStyleAttribute(element, propertyName);
        Test.assertTrue("actual[" + actual + "], expected[" + expected + "]", actual.indexOf( expected )!= -1);
    }

    public void testSetInlineBackgroundImageWithElementAlsoContainingABackgroundColour() {
        final String propertyName = StyleConstants.BACKGROUND_IMAGE;
        final String propertyValue = "image.gif";       
        final Colour colour = Colour.getColour( "red");

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute( element, StyleConstants.BACKGROUND_COLOR, colour.toCssColour() );

        StyleHelper.setInlineStyleProperty( element, propertyName, "url('" + propertyValue + "')");

        // check that the style got updated...
        final String expected = propertyValue;
        final String actual = DOM.getStyleAttribute(element, propertyName);
        Test.assertTrue("actual [" + actual + "] expected[" + expected + "]", actual.indexOf( expected )!= -1);

        final String backgroundColour = StyleHelper.getComputedStyleProperty(element, StyleConstants.BACKGROUND_COLOR); 
        final String expectedBackgroundColour = colour.toCssColour();
        Test.assertEquals( Colour.parse(expectedBackgroundColour), Colour.parse(backgroundColour ));

        final Colour actualColour = Colour.parse( backgroundColour );
        final Colour expectedColour = colour;
        Test.assertEquals( expectedColour, actualColour );        
    }    


    public void testGetComputedBackgroundPositionWhenNoValueIsSet() {
        final String propertyName = StyleConstants.BACKGROUND_POSITION;

        final Element element = this.createDivAndAddToDocument();        

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertTrue("" + element + ", actual[" + actual + "]", actual == null || actual.equals( "0% 0%") || actual.equals( "left left") || actual.equals( "0px 0px"));
    }


    public void testGetComputedBackgroundPosition() {
        final String propertyName = StyleConstants.BACKGROUND_POSITION;
        final String propertyValue = "0px 0px";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.BACKGROUND, "url('image.gif')");
        DOM.setStyleAttribute(element, StyleConstants.BACKGROUND_IMAGE, "url('image.gif')");
        DOM.setStyleAttribute(element, propertyName, propertyValue);    

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = propertyValue;
        Test.assertEquals(expected, actual);
    }

    public void testGetComputedBackgroundPositionWithElementThatIncludesAllTheOtherBackgroundProperties() {
        final String propertyName = StyleConstants.BACKGROUND_POSITION;
        final String propertyValue = "0px 0px";

        final Element element = this.createDivAndAddToDocument();
        final String backgroundProperty = "url('image.gif') no-repeat fixed #123456 " + propertyValue;
        DOM.setStyleAttribute(element, StyleConstants.BACKGROUND, backgroundProperty );

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = propertyValue;
        Test.assertEquals( backgroundProperty, expected, actual);
    }

    public void testGetComputedFontSizeSetToXSmallValue() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "x-small";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = "10px";
        Test.assertEquals(expected, actual);
    }

    public void testGetComputedFontSizeSetToSmallValue() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "small";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = "13px";
        Test.assertEquals(expected, actual);
    }

    public void testGetComputedFontSizeSetToMediumValue() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "medium";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = "16px";
        Test.assertEquals(expected, actual);
    }
    public void testGetComputedFontSizeSetToLargeValue() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "large";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = "18px";
        Test.assertEquals(expected, actual);
    }
    public void testGetComputedFontSizeSetToXLargeValue() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "x-large";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = "24px";
        Test.assertEquals(expected, actual);
    }
    public void testGetComputedFontSizeSetToXXLargeValue() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "xx-large";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        final String expected = "32px";
        Test.assertEquals(expected, actual);
    }
    public void testGetComputedFontSizeSetToSmaller() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "smaller";
        final int parentFontSize = 13;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(parent, propertyName, parentFontSize + "px");

        final Element child = DOM.createSpan();
        DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(child, propertyName, propertyValue);
        DOM.appendChild(parent, child);

        final String actualString = StyleHelper.getComputedStyleProperty( child, propertyName);
        Test.assertNotNull( actualString );

        final float expected =Math.round( parentFontSize * StyleSupportConstants.SMALLER_SCALING_FACTOR );
        TestRunner.log( "actual[" + actualString + "], expected[" + expected + "]");

        final float actual = Math.round(Double.parseDouble( actualString.substring( 0, actualString.length() - 2 )));
        Test.assertTrue( "actual[" + actual + "] expected[" + expected + "]", actual < parentFontSize );
        Test.assertEquals( "actual[" + actual + "] expected[" + expected + "]", expected, actual, 2.5f );
    }

    public void testGetComputedFontSizeSetToLarger() {
        final String propertyName = StyleConstants.FONT_SIZE;
        final String propertyValue = "larger";
        final int parentFontSize = 13;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(parent, propertyName, parentFontSize + "px");

        final Element child = DOM.createSpan();
        DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(child, propertyName, propertyValue);
        DOM.appendChild(parent, child);

        final String actualString = StyleHelper.getComputedStyleProperty( child, propertyName);
        Test.assertNotNull( actualString );

        final float expected =Math.round( parentFontSize * StyleSupportConstants.LARGER_SCALING_FACTOR );
        TestRunner.log( "actual[" + actualString + "], expected[" + expected + "]");

        final float actual = Math.round(Double.parseDouble( actualString.substring( 0, actualString.length() - 2 )));
        Test.assertTrue( "actual[" + actual + "] expected[" + expected + "]", actual > parentFontSize );
        Test.assertEquals( "actual[" + actual + "] expected[" + expected + "]", expected, actual, 2.5f );
    }

    public void testGetComputedFontWeightWithMissingPropertyValue() {
        final String propertyName = StyleConstants.FONT_WEIGHT;

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertEquals("" + element, "400", actual);
    }

    public void testGetComputedFontWeightWithNumberPropertyValue() {
        final String propertyName = StyleConstants.FONT_WEIGHT;

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setIntStyleAttribute(element, propertyName, 700);

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertEquals("" + element, "700", actual);
    }

    public void testGetComputedFontWeightSetToNormal(){
        final String propertyName = StyleConstants.FONT_WEIGHT;

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, "normal");

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertEquals("" + element, "400", actual);        
    }

    public void testGetComputedFontWeightSetToBold(){
        final String propertyName = StyleConstants.FONT_WEIGHT;

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
        DOM.setStyleAttribute(element, propertyName, "bold");

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertEquals("" + element, "700", actual);        
    }

    public void testGetComputedFontWeightSetToLighter() {
        final String propertyName = StyleConstants.FONT_WEIGHT;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(parent, propertyName, "bold");

        final Element child = DOM.createSpan();
        DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(child, propertyName, "lighter");
        DOM.appendChild(parent, child);

        final String actual = StyleHelper.getComputedStyleProperty( child, propertyName);
        Test.assertEquals( "" + child, "400", actual);
    }
    
    public void testGetComputedFontWeightSetToBolder() {
        final String propertyName = StyleConstants.FONT_WEIGHT;

        final Element parent = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(parent, propertyName, "normal");

        final Element child = DOM.createSpan();
        DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
        DOM.setStyleAttribute(child, propertyName, "bolder");
        DOM.appendChild(parent, child);

        final String actualString = StyleHelper.getComputedStyleProperty( child, propertyName);
        Test.assertNotNull( actualString );

        final String actual = StyleHelper.getComputedStyleProperty( child, propertyName);
        Test.assertEquals( "" + child, "700", actual);
    }
    
    
    public void testGetComputedBorderWidthThin() {
        final String propertyName = StyleConstants.BORDER_LEFT_WIDTH;
        final String propertyValue = "thin";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);
        DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_COLOR, "black");
        DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_STYLE, "solid");

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertNotNull( actual );
        TestRunner.log( actual );
        final int number = Integer.parseInt(actual.substring( 0, actual.length() - 2 )); 
        Test.assertTrue( "actual[" + actual + "]", number == StyleSupportConstants.BORDER_WIDTH_THIN_PX || number == StyleSupportConstants.BORDER_WIDTH_THIN_PX_IE6 );
    }  

    public void testGetComputedBorderWidthMedium() {
        final String propertyName = StyleConstants.BORDER_LEFT_WIDTH;
        final String propertyValue = "medium";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);
        DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_COLOR, "black");
        DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_STYLE, "solid");

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertNotNull( actual );
        final int number = Integer.parseInt(actual.substring( 0, actual.length() - 2 )); 
        Test.assertTrue( "actual[" + actual + "]", number == StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX || number == StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX_IE6 );
    }  

    public void testGetComputedBorderWidthThick() {
        final String propertyName = StyleConstants.BORDER_LEFT_WIDTH;
        final String propertyValue = "thick";

        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);
        DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_COLOR, "black");
        DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_STYLE, "solid");

        final String actual = StyleHelper.getComputedStyleProperty(element, propertyName);
        Test.assertNotNull( actual );
        final int number = Integer.parseInt(actual.substring( 0, actual.length() - 2 )); 
        Test.assertTrue( "actual[" + actual + "]", number == StyleSupportConstants.BORDER_WIDTH_THICK_PX || number == StyleSupportConstants.BORDER_WIDTH_THICK_PX_IE6 );
    }  
    public void testGetComputedStylePropertyNames0(){    
        final Element element = this.createDivAndAddToDocument();
        DOM.setStyleAttribute(element, StyleConstants.CURSOR, "move");
        DOM.setStyleAttribute(element, StringHelper.toCamelCase(StyleConstants.BACKGROUND_COLOR), "aquamarine");

        final String[] propertyNames = StyleHelper.getComputedStylePropertyNames( element );
        Test.assertNotNull( propertyNames );

        final int length = propertyNames.length;
        Test.assertTrue( "length: " + length, length >= 2 );

        final List list = new ArrayList();
        list.addAll( Arrays.asList( propertyNames ));

        Test.assertTrue(StyleConstants.CURSOR+ ", list: " + list, list.contains(StyleConstants.CURSOR) );
        Test.assertTrue(StyleConstants.BACKGROUND_COLOR+ ", list: " + list, list.contains(StringHelper.toCamelCase(StyleConstants.BACKGROUND_COLOR)) );
    }
    public void testGetComputedStylePropertyNames1(){
        final Element element = this.createDivAndAddToDocument(); 
        DOM.setStyleAttribute(element, StyleConstants.CURSOR, "move");
        DOM.setStyleAttribute(element, StringHelper.toCamelCase(StyleConstants.BACKGROUND), "url('image .gif')");
        DOM.setStyleAttribute(element, StringHelper.toCamelCase(StyleConstants.BACKGROUND_IMAGE), "url('image.gif')");

        final String[] propertyNames = StyleHelper.getComputedStylePropertyNames( element );
        Test.assertNotNull( propertyNames );

        final int length = propertyNames.length;
        Test.assertTrue( "length: " + length, length >= 2 );

        final List list = new ArrayList();
        list.addAll( Arrays.asList( propertyNames ));

        Test.assertTrue(StyleConstants.CURSOR+ ", list: " + list, list.contains(StyleConstants.CURSOR) );
        Test.assertTrue(StyleConstants.BACKGROUND_IMAGE+ ", list: " + list, list.contains(StringHelper.toCamelCase(StyleConstants.BACKGROUND_IMAGE)) ); 
    }

    /**
     * Creates a new div and adds it to the document.
     * No style properties or any other values are set.
     * @return
     */
    protected Element createDivAndAddToDocument() {
        final Element div = DOM.createDiv();
        DOM.setInnerHTML(div, this.getCurrentTestName());
        DOM.setStyleAttribute( div, StyleConstants.BACKGROUND_COLOR, "lime");
        this.addElement( div );
        return div;
    }

    protected void scrollIntoView( final Element element ){
        DOM.scrollIntoView(element );
        DomHelper.setFocus( element );
    }

    protected void onTestStarted(final Test test) {
        super.onTestStarted( test );

        if( false == this.getCurrentTestName().startsWith( "testConvert")){
            this.addTestNameDivider();
        }
    }
}
