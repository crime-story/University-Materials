using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Lab1ConsoleExample
{
    class Program
    {
        static void Main(string[] args)
        {
            //Classic Hello World
            Console.WriteLine("Hello World!");

            //UNCOMMENT TO FOLLOW METHOD

            //BasicDataTypeDeclarations();
            //BasicDataTypeDeclarationsUsingVar();
            //BasicOperationsBetweenInt();
            //BasicIfConditions();
            //BasicCollections();
            //BasicIterations();
            //BasicOperationsOnCollections();
            //BasicObjectInstantiation();
        }

        public static void BasicDataTypeDeclarations()
        {
            int value1 = 4;
            double value2 = 4.5;
            decimal value3 = 4m;
            string value4 = "Test";
        }

        public static void BasicDataTypeDeclarationsUsingVar()
        {
            var value5 = 4;
            var value6 = 4.5;
            var value7 = 4m;
            var value8 = "Test";
        }

        public static void BasicOperationsBetweenInt()
        {
            var x = 0;
            var y = 1;

            var sum = x + y; // 1
            var diff = x - y; //-1
            var produs = x * y; //0
            var impartire = x / y; // 0
            var mod = x % 10;
        }

        public static void BasicIfConditions()
        {
            var x = "TEST";
            
            if(x == "TEST") //este egal
            {
                Console.WriteLine("Correct");
            }
            else
            {
                Console.WriteLine("Incorrect.");
            }

            if(x != "TEST1") //este diferit
            {
                Console.WriteLine("Correct");
            }
            else
            {
                //do sth
            }

            // shorter If else condition in c#
            var y = x == "TEST" ? "DA" : "NU"; // y = DA 

            if(x == "TEST")
            {
                y = "DA";
            }
            else
            {
                y = "NU";
            }
        }

        public static void BasicCollections()
        {
            var intList = new List<int> { 1, 2, 3 };
            var stringList = new List<string> { "test1", "test2" };

            Console.WriteLine(intList[0]);
            Console.WriteLine(stringList[1]);
        }

        public static void BasicIterations()
        {
            var intList = new List<int> { 1, 2, 3 };
            var stringList = new List<string> { "test1", "test2" };

            for(var i = 0; i < intList.Count; i++)
            {
                Console.WriteLine(intList[i]);
            }

            foreach(var intElement in intList)
            {
                Console.WriteLine(intElement);
            }
        }

        //IEnumerable -> read and iterate;
        //=>
        //List

        public static void BasicOperationsOnCollections()
        {
            var intList = new List<int> { 1, 2, 3 };
            var stringList = new List<string> { "test1", "test2", "test21" };

            //Filter ints bigger than 1
            var filteredIntList = intList.Where(x => x > 1 && x < 100).ToList();
            

            var filteredIntList2 = new List<int>();
            foreach(var intValue in intList)
            {
                if (intValue > 1)
                    filteredIntList2.Add(intValue);
            }

            //Filter strings that contain the "test2" string
            var filteredStrings = stringList.Where(x => x.Contains("test2")).ToList();

            //Find first element that matches a condition
            var firstElement = stringList.FirstOrDefault(x => x.Contains("test2")); //returns test2
            //Find last element that matches a condition
            var lastElement = stringList.LastOrDefault(x => x.Contains("test2")); //returns test21
           
            var firstElementNotfound = stringList.FirstOrDefault(x => x.Contains("tenis")); //returns null

            //Add in a list
            intList.Add(5);

            //Count
            intList.Count();

            //Remove
            intList.Remove(1); //removes the first element that matches the value 1

        }

        public static void BasicObjectInstantiation()
        {
            var objectTest = new TestClass();

            objectTest.ValueIntTest = 9;
            objectTest.ValueStringTest = "Test";

            //Added Newtonsoft Package to use Json serialization on an object
            //Visual Studio upper bar => Tools => Nugget Package Manager => Manage Nuget Packages for Solution 
            var objectTestSerialized = JsonConvert.SerializeObject(objectTest);

            Console.WriteLine(objectTestSerialized);
        }

        //Example of a Class shown for C# syntax
        public class TestClass
        {
            public string ValueStringTest { get; set; }
            public int ValueIntTest { get; set; }
        }
    }
}
