/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2016 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.github.javaparser.javadoc;

import com.github.javaparser.javadoc.description.JavadocDescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavadocParserTest {

    @Test
    public void parseSimplestContent() {
        assertEquals(new JavadocDocument(JavadocDescription.parseText("A simple line of text")),
                new JavadocParser().parse("A simple line of text"));
    }

    @Test
    public void parseSingleLineWithSpacing() {
        assertEquals(new JavadocDocument(JavadocDescription.parseText("The line number of the first character of this Token.")),
                new JavadocParser().parse(" The line number of the first character of this Token. "));
    }

    @Test
    public void parseSingleLineWithNewLines() {
        assertEquals(new JavadocDocument(JavadocDescription.parseText("The string image of the token.")),
                new JavadocParser().parse("\n" +
                        "   * The string image of the token.\n" +
                        "   "));
    }

    @Test
    public void parseCommentWithNewLines() {
        String text = "\n" +
                "   * The version identifier for this Serializable class.\n" +
                "   * Increment only if the <i>serialized</i> form of the\n" +
                "   * class changes.\n" +
                "   ";
        assertEquals(new JavadocDocument(JavadocDescription.parseText("The version identifier for this Serializable class.\n" +
                        "Increment only if the <i>serialized</i> form of the\n" +
                        "class changes.")),
                new JavadocParser().parse(text));
    }

    @Test
    public void parseCommentWithIndentation() {
        String text = "Returns a new Token object, by default.\n" +
                "   * However, if you want, you can create and return subclass objects based on the value of ofKind.\n" +
                "   *\n" +
                "   *    case MyParserConstants.ID : return new IDToken(ofKind, image);\n" +
                "   *\n" +
                "   * to the following switch statement. Then you can cast matchedToken";
        assertEquals(new JavadocDocument(JavadocDescription.parseText("Returns a new Token object, by default.\n" +
                        "However, if you want, you can create and return subclass objects based on the value of ofKind.\n" +
                                "\n" +
                                "   case MyParserConstants.ID : return new IDToken(ofKind, image);\n" +
                                "\n" +
                                "to the following switch statement. Then you can cast matchedToken")),
                new JavadocParser().parse(text));
    }

    @Test
    public void parseBlockTagsAndEmptyDescription() {
        String text = "\n" +
                "   * @deprecated\n" +
                "   * @see #getEndColumn\n" +
                "   ";
        assertEquals(new JavadocDocument(JavadocDescription.parseText(""))
                .addBlockTag(new JavadocBlockTag(JavadocBlockTag.Type.DEPRECATED, ""))
                .addBlockTag(new JavadocBlockTag(JavadocBlockTag.Type.SEE, "#getEndColumn")), new JavadocParser().parse(text));
    }

    @Test
    public void parseParamBlockTags() {
        String text = "\n" +
                "     * Add a field to this and automatically add the import of the type if needed\n" +
                "     *\n" +
                "     * @param typeClass the type of the field\n" +
                "     * @param name the name of the field\n" +
                "     * @param modifiers the modifiers like {@link Modifier#PUBLIC}\n" +
                "     * @return the {@link FieldDeclaration} created\n" +
                "     ";
        JavadocDocument res = new JavadocParser().parse(text);
        assertEquals(new JavadocDocument(JavadocDescription.parseText("Add a field to this and automatically add the import of the type if needed"))
                .addBlockTag(JavadocBlockTag.createParamBlockTag("typeClass", "the type of the field"))
                .addBlockTag(JavadocBlockTag.createParamBlockTag("name", "the name of the field"))
                .addBlockTag(JavadocBlockTag.createParamBlockTag("modifiers", "the modifiers like {@link Modifier#PUBLIC}"))
                .addBlockTag(new JavadocBlockTag(JavadocBlockTag.Type.RETURN, "the {@link FieldDeclaration} created")), res);
    }

    @Test
    public void startsWithAsteriskEmpty() {
        assertEquals(-1, JavadocParser.startsWithAsterisk(""));
    }

    @Test
    public void startsWithAsteriskNoAsterisk() {
        assertEquals(-1, JavadocParser.startsWithAsterisk(" ciao"));
    }

    @Test
    public void startsWithAsteriskAtTheBeginning() {
        assertEquals(0, JavadocParser.startsWithAsterisk("* ciao"));
    }

    @Test
    public void startsWithAsteriskAfterSpaces() {
        assertEquals(3, JavadocParser.startsWithAsterisk("   * ciao"));
    }

}
