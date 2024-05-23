import chainlit as cl
import requests

endpoint = 'http://localhost:8080/ai/callWithContext'

@cl.on_message
async def main(message: cl.Message):
    # Send a response back to the user
    requestsParam = {'message': requests.utils.quote(message.content)}
    response = requests.get(endpoint, params=requestsParam)
    responseContent = response.content.decode('utf-8')
    await cl.Message(
        content=f"{responseContent}",
    ).send()