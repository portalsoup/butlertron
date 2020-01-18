/**
 * Additional plug-n-play functionality for commands.  Delegated functionality is achieved with an interface defining
 * the type of functionality, and an implementor which provides one of perhaps multiple concrete implementations of
 * that type.
 *
 * In the following example, the command instance will have access to methods of the Identifiable interface as
 * implemented by the Identifier class.
 *
 * // type
 * interface Identifiable {
 *     // Method signatures here
 * }
 *
 * // implementor
 * class Identifier : Identifiable {
 *     // Implement the methods
 * }
 *
 * // command
 * class MyCommand : MyBaseCommand, Identifiable by Identifier() {
 *
 * }
 */
package com.portalsoup.mrbutlertron.core.command.delegates;