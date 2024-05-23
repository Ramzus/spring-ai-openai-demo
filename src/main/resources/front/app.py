import chainlit as cl
import requests


@cl.on_message
async def main(message: cl.Message):
    # Send a response back to the useré
    response = requests.get('http://localhost:8080/api/callWithContext', params=message)
    await cl.Message(
        content=f"{message.content}",
    ).send()