# Custom Language Lexical Analyzer

A comprehensive lexical analyzer (lexer) implementation for a custom programming language, featuring tokenization, symbol table generation, NFA/DFA construction, and error handling.

## Features

### Core Functionality
- **Lexical Analysis**: Complete tokenization of custom language source code
- **Symbol Table Generation**: Automatic symbol table construction with scope tracking
- **Error Handling**: Comprehensive error detection and reporting
- **NFA/DFA Construction**: Finite automata generation from tokens
- **Multi-line Comment Support**: Handles both single-line (`//`) and multi-line (`/* */`) comments

### Supported Language Constructs
- **Data Types**: `int`, `decimal`, `bool`, `char`, `string`
- **Operators**: Arithmetic (`+`, `-`, `*`, `/`, `%`, `^`), Relational (`==`, `!=`, `<`, `>`, `<=`, `>=`), Logical (`&&`, `||`, `!`)
- **Control Structures**: `if`, `else`, `return`
- **I/O Operations**: `read`, `write`
- **Literals**: Integer, decimal, string, character, boolean (`true`, `false`)
- **Comments**: Single-line and multi-line

## Project Structure

```
src/
├── Demo.java              # Main driver class
├── LexicalAnalyzer.java   # Core lexical analyzer implementation
├── Token.java             # Token representation
├── SymbolTable.java       # Symbol table management
├── Symbol.java            # Symbol representation
├── ErrorHandler.java      # Error handling and reporting
├── NFA.java              # Non-deterministic Finite Automaton
├── DFA.java              # Deterministic Finite Automaton
├── State.java            # State representation for DFA
└── input.customlanguage  # Sample input file
```

## Installation & Setup

### Prerequisites
- Java JDK 8 or higher
- IDE (Eclipse, IntelliJ IDEA, or VS Code with Java extensions)

### Running the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/custom-language-lexical-analyzer.git
   cd custom-language-lexical-analyzer
   ```

2. Compile the Java files:
   ```bash
   javac -d bin src/Assignment1/*.java
   ```

3. Update the file path in `Demo.java` to point to your input file:
   ```java
   BufferedReader br = new BufferedReader(new FileReader("path/to/your/input.customlanguage"))
   ```

4. Run the analyzer:
   ```bash
   java -cp bin Assignment1.Demo
   ```

## Input File Format

The lexical analyzer processes `.customlanguage` files. Here's a sample structure:

```javascript
int counter = 100;
decimal value = 123.45678;
bool flag = true;
char letter = 'a';

int main() {
    int sum = counter + 50;
    if (counter > 50) {
        write("Counter is greater than 50");
    }
    // This is a single-line comment
    /* This is a 
       multi-line comment */
}
```

## Language Rules & Constraints

### Variable Naming Rules
- ✅ Must start with lowercase letter
- ✅ Can contain lowercase letters only
- ❌ Cannot contain uppercase letters
- ❌ Cannot contain digits
- ❌ Cannot contain underscores

### Supported Features
- **Scope Detection**: Global vs Local variable tracking
- **Nested Structures**: Support for nested if statements
- **Memory Location Assignment**: Automatic memory location allocation (starting from 1000)
- **Comprehensive Error Reporting**: Line-by-line error detection

## Output

The analyzer provides detailed output including:

### 1. Token List
```
Token {type='KEYWORD', lexeme='int', line='1'}
Token {type='IDENTIFIER', lexeme='counter', line='1'}
Token {type='ASSIGNMENT', lexeme='=', line='1'}
Token {type='INTEGER', lexeme='100', line='1'}
```

### 2. Symbol Table
```
Name                 | Type                 | Scope      | Memory Location
---------------------------------------------------------------------------
counter              | IDENTIFIER           | Global     | 1000
value                | IDENTIFIER           | Global     | 1001
```

### 3. NFA Transition Table
```
State 0 --i--> State 1
State 2 --n--> State 3
State 4 --t--> State 5
```

### 4. DFA State Mappings
```
DFA State: 0 corresponds to NFA states: [0, 2, 4]
DFA State: 1 corresponds to NFA states: [1, 3, 5]
```

### 5. Statistics
- Total Tokens: 45
- Total Unique States: 12
- Total Operators Used: 8
- Total Keywords Used: 6

### 6. Error Reports
```
Error at line 5: Invalid variable name: 'Invalid' (variable names cannot contain capital letters)
Error at line 6: Invalid variable name: '12re' (variable names cannot contain digits)
```

## Technical Implementation

### Key Classes

#### LexicalAnalyzer
- Main tokenization logic
- Handles preprocessing, token extraction, and error detection
- Manages scope tracking and symbol table population

#### Token
- Represents individual tokens with type, lexeme, and line number
- Supports various token types (KEYWORD, IDENTIFIER, OPERATOR, etc.)

#### SymbolTable & Symbol
- Manages symbol storage and retrieval
- Tracks symbol name, type, scope, and memory location

#### NFA & DFA
- Implements finite automaton construction
- Supports NFA to DFA conversion using subset construction algorithm

#### ErrorHandler
- Centralized error collection and reporting
- Provides detailed error messages with line numbers

### Algorithms Used
- **Finite Automaton Construction**: Thompson's construction for NFA
- **NFA to DFA Conversion**: Subset construction algorithm
- **Lexical Analysis**: Character-by-character scanning with lookahead

## Testing

The project includes comprehensive test cases covering:
- Valid token recognition
- Error detection for invalid constructs
- Scope tracking accuracy
- Comment handling (single-line and multi-line)
- Number format validation
- String and character literal processing

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

