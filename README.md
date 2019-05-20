# Serverless - Airline Seat Planner
[![Build Status](https://travis-ci.org/benkelly/Airline-Seat-Planner-Aws-Lambda.svg?branch=master)](https://travis-ci.org/benkelly/Airline-Seat-Planner-Aws-Lambda)

## Table of contents
- [Serverless - Airline Seat Planner](#serverless---airline-seat-planner)
  - [Table of contents](#table-of-contents)
- [Intro](#intro)
  - [Sorting Approaches Taken](#sorting-approaches-taken)
      - [Conditional Naive Implementation](#conditional-naive-implementation)
      - [Monte Carlo based decision Implementation](#monte-carlo-based-decision-implementation)
    - [Comparison](#comparison)
- [AWS Lambda](#aws-lambda)
    - [curl example:](#curl-example)
    - [required](#required)
    - [Nice to have](#nice-to-have)
- [Getting Started](#getting-started)
  - [continuous integration (CI)](#continuous-integration-ci)
  - [Sample JSON input:](#sample-json-input)
  - [Java Stack](#java-stack)
    - [Testing](#testing)
  - [AWS Stack](#aws-stack)

# Intro

This Airline Seating planner aims to keep passenger satisfaction high by accommodating their seating preferences (such as window seat and group seating), when sorting out the seating plan.

I solved this problem with 2 different sorting methods, discussed [below](#Sorting Approaches Taken).

This logic was also implemented in to a Serverless application and deployed to [AWS Lambda](#AWS Lambda), which can be tested on right now.

## Sorting Approaches Taken
I decided to try out two different routes of solving this sorting problem:

#### Conditional Naive Implementation

This implementation aims to satisfy the most important passengers first (i.e. the largest groups with Window preferences). This is done by sorting their seats first and descending through the passenger groups in that pattern, all while comparing the possible satisfaction for each possible row based on whether that current row will satisfy all their needs, factoring in the already seated passengers in window seats and such.

#### Monte Carlo based decision Implementation

This is a far less complex implementation, but I was curious how this type of decision algorithm would compare to the Naive implementation, as I felt that the short list of conditional requirements for the passenger's criteria plus the
real life defined max size of passengers and airplane could carry would be small enough to compute in a reasonable time. For this implementation, the Monte Carlo algorithm simply cares about the final overall passenger satisfaction score, it picks the seat rows at random, it will move a window preference passenger in a group to a window seat but it doesn't aim to, That's only out of luck and the the overall satisfaction score reflects if the passenger's criteria wasn't met. The algorithm will constantly sort will until it gets a satisfaction of 100% or run out of epochs when it will then take its best results.

(The largest current commercial airplane is the Airbus A380 which can carry 544 people 🤓)

### Comparison

|  Data file       |  seat number |    Naive  (ms)   |  Monte Carlo (ms) |
| :---             |  :---:       |     :---:        |    :---:          |
| base_case.txt    | 16           | ~ 017            | ~ 003             |
| overkill_case.txt| 1000         | ~ 021           | ~ 004             |

# AWS Lambda

I felt that this application would serve a much better application if accessible through an API and that a 
light weight serverless architecture was the best fit.
 
I chose to also use the [Spring Cloud Function framework](https://spring.io/projects/spring-cloud-function) as I love Spring and also there idea of aiming to create an almost cloud agnostic framework for serverless applications and such.

### curl example:
```bash
curl --request POST \
  --url https://api.benjamin.ie/asp \
  --header 'x-api-key: Sg6iTu695K5X42tm43mKO20YpK8jx86C7omvTR2m' \
  --data '{"rowSize":4,"rowHeight":4,"groupList":[{"passengerList":[{"windowPref":true,"name":"1"},{"windowPref":false,"name":"2"},{"windowPref":false,"name":"3"}]},{"passengerList":[{"windowPref":false,"name":"4"},{"windowPref":false,"name":"5"},{"windowPref":false,"name":"6"},{"windowPref":false,"name":"7"}]},{"passengerList":[{"windowPref":false,"name":"8"}]},{"passengerList":[{"windowPref":false,"name":"9"},{"windowPref":false,"name":"10"},{"windowPref":true,"name":"11"}]},{"passengerList":[{"windowPref":true,"name":"12"}]},{"passengerList":[{"windowPref":false,"name":"13"},{"windowPref":false,"name":"14"}]},{"passengerList":[{"windowPref":false,"name":"15"},{"windowPref":false,"name":"16"}]}]}'
```
  PS i like to pipe `|` with [./jq](https://stedolan.github.io/jq/)`| jq '.'` or just simply `| python -m json.tool` 
  to make the JSON a little nicer! 
  :nail_care:

![](readme_images/aws_api_curl.gif)

__Everyone__: "Hey? I just curled this api and it took a while?"

__me__: "Yeah i know.. curl it again? much faster? Yay.. One of the few drawbacks of serverless architecture, where theres such thing as a "Cold start & Warm start" when It comes to invoking a serverless function.. here a [link](https://blog.octo.com/en/cold-start-warm-start-with-aws-lambda/) to a nice article about it!


### required
- Java 8
- Maven 3
- Aws Cli
### Nice to have
- Docker
- Sam Cli

# Getting Started
1. clone repo and cd into it.
    ```bash
    git clone git@github.com:benkelly/Airline-Seat-Planner-Aws-Lambda.git
    ```
    ```bash
    cd Airline-Seat-Planner-Aws-Lambda
    ```
2. install packages and run test.
    ```bash
    mvn clean install
    ```
3. Run the command line version; run this command from the projects dir to get the gist:
    ```bash
    java -jar target/Airline-Seat-Planner-0.0.1-SNAPSHOT-local.jar --f=target/classes/sample_data/base_case.txt
    ```
    ![](readme_images/local_runnig.gif)
    
    <h4 id="springApi">
4. Want more fun? like to try the Api locally just through Spring?
    -   Re-run maven with the following following profile `api` 
    </h4>

    ```bash
    mvn clean install  -DskipTests -Papi
    ```
    - run the new version jar:
    ```bash
    java -jar target/Airline-Seat-Planner-0.0.1-SNAPSHOT-api.jar
    ```
    - Then in another Terminal window run a `curl` command like:
    ```bash
    curl -d @input.json -H "Content-Type: application/json" http://localhost:8080/AirlineSeatPlannerLambdaFunction
    ```
    note: the `@input.json` refers to the file in the root of this project, which is also a good reference to the 
    json schema for the input data JSON! :neckbeard:
    
5. If you are even up for some more fun you can run it through Sam-cli:
    - I won't go into much detail about running this project though as you'll be essentially repeating step [4](#springApi) plus there's loads of great guides out there!
        - Either way I've supplied a `template.yaml` to get the ball running! :^)






## continuous integration (CI) 

One of the best and worst things about building for AWS Lambda is the "agileness" of editing and re-deployment which 
is great for tinkering with you weekend hobby of making a homemade smart kettle but for Industry; local development 
with testing and CI piplines to devqa and then prod versions is king.

### heres a little example of how this could be implemented in to a CI pipeline like Jenkins :heart_eyes:

```groovy
if (env.BRANCH_NAME == "RELEASE_BRANCH") {
    node("YOUR_SLAVE_WITH_AWS_CREDS") {

        stage("Checkout") {
            checkout scm
            sh "git checkout RELEASE_BRANCH"
        }

        stage("Build/Test") {
            sh 'mvn clean install -Pdevqa'
        }

        stage("Publish/Deploy") {
            stash includes: '**/target/*-aws.jar', name: 'app'
            sh "mv  target/*aws.jar lambda.jar"
            sh "aws lambda update-function-code --function-name Lambda-Function-Name --region us-east-1 --zip-file fileb://lambda.jar"
        }
    }
}
```


## Sample JSON input:
```json
{
  "rowSize": 4,
  "rowHeight": 4,
  "groupList": [
    {
      "passengerList": [
        {
          "windowPref": true,
          "name": "1"
        },
        {
          "windowPref": false,
          "name": "2"
        },
        {
          "windowPref": false,
          "name": "3"
        }
      ]
    },
    {
      "passengerList": [
        {
          "windowPref": false,
          "name": "4"
        },
        {
          "windowPref": false,
          "name": "5"
        },
        {
          "windowPref": false,
          "name": "6"
        },
        {
          "windowPref": false,
          "name": "7"
        }
      ]
    },
    {
      "passengerList": [
        {
          "windowPref": false,
          "name": "8"
        }
      ]
    },
    {
      "passengerList": [
        {
          "windowPref": false,
          "name": "9"
        },
        {
          "windowPref": false,
          "name": "10"
        },
        {
          "windowPref": true,
          "name": "11"
        }
      ]
    },
    {
      "passengerList": [
        {
          "windowPref": true,
          "name": "12"
        }
      ]
    },
    {
      "passengerList": [
        {
          "windowPref": false,
          "name": "13"
        },
        {
          "windowPref": false,
          "name": "14"
        }
      ]
    },
    {
      "passengerList": [
        {
          "windowPref": false,
          "name": "15"
        },
        {
          "windowPref": false,
          "name": "16"
        }
      ]
    }
  ]
}
```

## Java Stack
- Spring
- Lombok
- Aws Lambda handler
### Testing
- JUnit 5
- Mockito
- Hamcrest

## AWS Stack
- API Gateway
- Lambda





:octocat:
